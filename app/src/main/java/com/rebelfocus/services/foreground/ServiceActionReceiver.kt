package com.rebelfocus.services.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rebelfocus.core.domain.usecase.PauseSessionUseCase
import com.rebelfocus.core.domain.usecase.ResumeSessionUseCase
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase
import com.rebelfocus.core.domain.usecase.StopReason
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServiceActionReceiver : BroadcastReceiver() {

    @Inject lateinit var pauseSessionUseCase: PauseSessionUseCase
    @Inject lateinit var resumeSessionUseCase: ResumeSessionUseCase
    @Inject lateinit var stopFocusSessionUseCase: StopFocusSessionUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val sessionId = intent.getStringExtra(NotificationHelper.EXTRA_SESSION_ID) ?: return
        
        when (intent.action) {
            NotificationHelper.ACTION_PAUSE -> {
                scope.launch { pauseSessionUseCase(sessionId) }
            }
            NotificationHelper.ACTION_RESUME -> {
                scope.launch { resumeSessionUseCase(sessionId) }
            }
            NotificationHelper.ACTION_STOP -> {
                scope.launch { stopFocusSessionUseCase(sessionId, StopReason.Complete) }
            }
        }
    }
}
