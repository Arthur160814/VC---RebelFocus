package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.data.repository.ScheduledTriggerRepository
import com.rebelfocus.core.domain.calendar.CalendarSource
import com.rebelfocus.core.domain.scheduling.AlarmScheduler
import com.rebelfocus.core.model.ScheduleType
import com.rebelfocus.core.model.ScheduledTrigger
import com.rebelfocus.core.model.ScheduledTriggerStatus
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncCalendarEventsUseCase @Inject constructor(
    private val ruleRepository: AutomationRuleRepository,
    private val triggerRepository: ScheduledTriggerRepository,
    private val calendarSource: CalendarSource,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        val nowMillis = System.currentTimeMillis()
        val endWindowMillis = nowMillis + TimeUnit.DAYS.toMillis(7) // Fetch 7 days ahead

        // 1. Get all enabled calendar rules
        val allRules = ruleRepository.observeEnabled().first()
        val calendarRules = allRules.filter { it.scheduleType == ScheduleType.CalendarEvent && it.calendarMatchConfig != null }

        for (rule in calendarRules) {
            val config = rule.calendarMatchConfig!!
            
            // 2. Fetch events
            val events = calendarSource.getEvents(config.calendarId, nowMillis, endWindowMillis)
            
            // 3. Filter events
            val matchedEvents = events.filter { event ->
                // Check keyword
                val matchesKeyword = config.titleKeyword.isNullOrBlank() || 
                    event.title.contains(config.titleKeyword, ignoreCase = true)
                
                // Check busy status
                val matchesBusy = !config.requireBusyStatus || event.isBusy

                // Check time window
                val matchesTimeWindow = checkTimeWindow(event.startTimeMillis, config.timeWindowStartMinutes, config.timeWindowEndMinutes)

                matchesKeyword && matchesBusy && matchesTimeWindow
            }

            // 4. Map to trigger identities
            val activeTriggerIdsThisSync = mutableSetOf<String>()

            for (event in matchedEvents) {
                // Identity: ruleId + eventId + startTime (in case recurring event instances share the same EVENT_ID but diff start times)
                val triggerId = "${rule.id}_${event.id}_${event.startTimeMillis}"
                activeTriggerIdsThisSync.add(triggerId)

                val existing = triggerRepository.getById(triggerId)

                val newTrigger = ScheduledTrigger(
                    id = triggerId,
                    sourceRuleId = rule.id,
                    sourceEventId = event.id,
                    triggerAtMillis = event.startTimeMillis,
                    profileId = rule.profileId,
                    status = ScheduledTriggerStatus.PENDING,
                    requestCode = triggerId.hashCode(),
                    createdAt = existing?.createdAt ?: nowMillis,
                    updatedAt = nowMillis
                )

                // UPSERT into DB
                triggerRepository.insertOrReplace(newTrigger)

                // Schedule alarm (overwrites if request code matches)
                alarmScheduler.scheduleExact(triggerId, event.startTimeMillis, newTrigger.requestCode)
            }

            // 5. Cleanup stale triggers for THIS rule
            val pendingTriggers = triggerRepository.getByRuleIdAndStatus(rule.id, ScheduledTriggerStatus.PENDING)
            val staleTriggers = pendingTriggers.filter { it.id !in activeTriggerIdsThisSync }

            for (stale in staleTriggers) {
                // Mark cancelled
                triggerRepository.update(stale.copy(
                    status = ScheduledTriggerStatus.CANCELLED,
                    updatedAt = nowMillis
                ))
                // Cancel OS alarm
                alarmScheduler.cancel(stale.id, stale.requestCode)
            }
        }
    }

    private fun checkTimeWindow(eventStartMillis: Long, startMins: Int?, endMins: Int?): Boolean {
        if (startMins == null || endMins == null) return true
        
        val zdt = Instant.ofEpochMilli(eventStartMillis).atZone(ZoneId.systemDefault())
        val eventMinsFromMidnight = zdt.hour * 60 + zdt.minute

        return if (startMins <= endMins) {
            eventMinsFromMidnight in startMins..endMins
        } else {
            // crosses midnight (e.g., 22:00 to 06:00)
            eventMinsFromMidnight >= startMins || eventMinsFromMidnight <= endMins
        }
    }
}
