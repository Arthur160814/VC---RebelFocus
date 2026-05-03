package com.rebelfocus.core.domain.scheduling

import com.rebelfocus.core.model.AutomationRule
import com.rebelfocus.core.model.ScheduleType
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

import javax.inject.Inject

/**
 * Calculates the exact epoch millis for the next occurrence of a schedule.
 */
class NextOccurrenceCalculator @Inject constructor() {

    /**
     * Returns the next epoch millis for the rule, or null if it should no longer be scheduled.
     */
    fun calculateNextOccurrence(rule: AutomationRule, nowMillis: Long = System.currentTimeMillis()): Long? {
        if (!rule.isEnabled) return null

        val now = Instant.ofEpochMilli(nowMillis).atZone(ZoneId.systemDefault())

        return when (rule.scheduleType) {
            ScheduleType.OneTime -> calculateOneTime(rule, now)
            ScheduleType.RecurringWeekday -> calculateRecurring(rule, now)
            ScheduleType.CalendarEvent -> null // Phase 6 excludes calendar integration
        }
    }

    private fun calculateOneTime(rule: AutomationRule, now: ZonedDateTime): Long? {
        val startMins = rule.startTimeMinutes ?: return null
        
        val scheduledTimeToday = now.toLocalDate().atStartOfDay(now.zone)
            .plusMinutes(startMins.toLong())

        return if (scheduledTimeToday.isAfter(now)) {
            scheduledTimeToday.toInstant().toEpochMilli()
        } else {
            // It was already missed today. For OneTime, we can schedule it for tomorrow
            // if the user just created it, but typically OneTime means a specific date.
            // Since our model only stores `startTimeMinutes` without a date for OneTime,
            // we assume it means "the next occurrence of this time".
            scheduledTimeToday.plusDays(1).toInstant().toEpochMilli()
        }
    }

    private fun calculateRecurring(rule: AutomationRule, now: ZonedDateTime): Long? {
        val startMins = rule.startTimeMinutes ?: return null
        if (rule.daysOfWeek.isEmpty()) return null

        // daysOfWeek: 1=Monday, 7=Sunday
        val activeDays = rule.daysOfWeek.map { DayOfWeek.of(it) }.toSet()
        
        var nextTime = now.toLocalDate().atStartOfDay(now.zone).plusMinutes(startMins.toLong())
        
        // If the target time today has already passed, we must look starting from tomorrow.
        if (!nextTime.isAfter(now)) {
            nextTime = nextTime.plusDays(1)
        }

        // Keep adding days until we hit one of the target days of the week
        var daysChecked = 0
        while (nextTime.dayOfWeek !in activeDays) {
            nextTime = nextTime.plusDays(1)
            daysChecked++
            if (daysChecked > 7) return null // Safety net
        }

        return nextTime.toInstant().toEpochMilli()
    }
}
