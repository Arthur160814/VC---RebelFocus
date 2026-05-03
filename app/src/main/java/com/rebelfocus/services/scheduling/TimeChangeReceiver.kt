package com.rebelfocus.services.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.domain.logging.DiagnosticLogger
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase
import com.rebelfocus.core.model.ScheduleType
import com.rebelfocus.services.calendar.CalendarSyncWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimeChangeReceiver : BroadcastReceiver() {

    @Inject lateinit var logger: DiagnosticLogger
    @Inject lateinit var ruleRepository: AutomationRuleRepository
    @Inject lateinit var scheduleSessionUseCase: ScheduleSessionUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == "android.intent.action.TIME_SET" || action == "android.intent.action.TIMEZONE_CHANGED") {
            val pendingResult = goAsync()
            
            scope.launch {
                try {
                    logger.info("SYSTEM", "Time change detected: $action. Refreshing all schedules.")

                    // 1. Re-sync calendar triggers
                    val syncRequest = OneTimeWorkRequestBuilder<CalendarSyncWorker>().build()
                    WorkManager.getInstance(context)
                        .enqueueUniqueWork(
                            CalendarSyncWorker.WORK_NAME_ONDEMAND,
                            ExistingWorkPolicy.REPLACE,
                            syncRequest
                        )

                    // 2. Re-schedule manual rules
                    val rules = ruleRepository.observeAll().first()
                    rules.filter { it.isEnabled && it.scheduleType != ScheduleType.CalendarEvent }
                        .forEach { scheduleSessionUseCase(it) }
                        
                } catch (e: Exception) {
                    logger.error("SCHEDULING", "Failed to refresh manual schedules after time change", e.message)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
