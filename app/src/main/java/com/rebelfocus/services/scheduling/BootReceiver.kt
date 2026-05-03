package com.rebelfocus.services.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            // Use WorkManager for deferred boot-time restoration.
            // WorkManager is not the exact trigger mechanism itself, 
            // but it handles re-registering the exact alarms safely without hitting ANRs.
            val workRequest = OneTimeWorkRequestBuilder<RestoreSchedulesWorker>().build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
