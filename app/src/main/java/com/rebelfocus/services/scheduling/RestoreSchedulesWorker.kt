package com.rebelfocus.services.scheduling

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase
import com.rebelfocus.core.domain.usecase.SyncCalendarEventsUseCase
import com.rebelfocus.core.model.ScheduleType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

/**
 * Boot-recovery worker that restores all enabled manual schedule alarms
 * and triggers a calendar sync to re-materialize calendar-derived triggers.
 */
@HiltWorker
class RestoreSchedulesWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val ruleRepository: AutomationRuleRepository,
    private val scheduleSessionUseCase: ScheduleSessionUseCase,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            // Fetch all rules
            val rules = ruleRepository.observeAll().first()

            // Re-schedule enabled manual (non-calendar) rules
            for (rule in rules) {
                if (rule.isEnabled && rule.scheduleType != ScheduleType.CalendarEvent) {
                    scheduleSessionUseCase(rule)
                }
            }

            // Re-sync calendar-derived triggers
            syncCalendarEventsUseCase()

            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.retry()
        }
    }
}
