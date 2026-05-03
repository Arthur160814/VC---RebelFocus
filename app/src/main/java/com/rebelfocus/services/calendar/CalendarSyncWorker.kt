package com.rebelfocus.services.calendar

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rebelfocus.core.domain.usecase.SyncCalendarEventsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager job for periodic and on-demand calendar event syncing.
 *
 * Invoked:
 *  - Periodically every 12 hours via PeriodicWorkRequest
 *  - On boot via RestoreSchedulesWorker
 *  - On-demand when a calendar rule is created/edited or READ_CALENDAR is granted
 */
@HiltWorker
class CalendarSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_NAME_PERIODIC = "calendar_sync_periodic"
        const val WORK_NAME_ONDEMAND = "calendar_sync_ondemand"
    }

    override suspend fun doWork(): Result {
        return try {
            syncCalendarEventsUseCase()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
