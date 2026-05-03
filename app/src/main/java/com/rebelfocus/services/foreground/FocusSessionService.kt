package com.rebelfocus.services.foreground

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FocusSessionService : Service() {

    @Inject lateinit var sessionRepository: SessionRepository
    @Inject lateinit var notificationHelper: NotificationHelper

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        private const val ACTION_START = "com.rebelfocus.action.START_SERVICE"
        private const val ACTION_STOP = "com.rebelfocus.action.STOP_SERVICE"

        fun start(context: Context) {
            val intent = Intent(context, FocusSessionService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, FocusSessionService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Ensure we only start if there is an active session
                scope.launch {
                    val session = sessionRepository.getActiveSession()
                    if (session != null) {
                        startForegroundWithNotification(session)
                        observeSession()
                    } else {
                        stopSelf()
                    }
                }
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundWithNotification(session: Session) {
        val notification = notificationHelper.buildNotification(session)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NotificationHelper.NOTIFICATION_ID, 
                notification, 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) 
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE 
                else 0
            )
        } else {
            startForeground(NotificationHelper.NOTIFICATION_ID, notification)
        }
    }

    @Inject lateinit var timingCalculator: com.rebelfocus.core.domain.session.SessionTimingCalculator
    @Inject lateinit var transitionToBreakUseCase: com.rebelfocus.core.domain.usecase.TransitionToBreakUseCase
    @Inject lateinit var transitionToActiveFocusUseCase: com.rebelfocus.core.domain.usecase.TransitionToActiveFocusUseCase
    @Inject lateinit var stopFocusSessionUseCase: com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase
    @Inject lateinit var diagnosticLogger: com.rebelfocus.core.domain.logging.DiagnosticLogger

    private var tickerJob: kotlinx.coroutines.Job? = null

    private fun observeSession() {
        scope.launch {
            sessionRepository.observeActiveSession().collect { session ->
                if (session == null || isTerminalState(session.state)) {
                    tickerJob?.cancel()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                } else {
                    startTicker(session)
                }
            }
        }
    }

    private fun startTicker(session: Session) {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (true) {
                notificationHelper.updateNotification(session)
                
                // Auto-transition logic
                val now = android.os.SystemClock.elapsedRealtime()
                when (session.state) {
                    SessionState.ActiveFocus -> {
                        if (timingCalculator.isFocusIntervalComplete(session, now)) {
                            handleFocusIntervalComplete(session)
                        }
                    }
                    SessionState.Break -> {
                        if (timingCalculator.isBreakComplete(session, now)) {
                            handleBreakComplete(session)
                        }
                    }
                    else -> { /* No auto-transition for other states */ }
                }

                kotlinx.coroutines.delay(1000)
            }
        }
    }

    private suspend fun handleFocusIntervalComplete(session: Session) {
        val isFinalInterval = session.pomodoroCount + 1 >= session.pomodoroTarget
        if (isFinalInterval) {
            diagnosticLogger.info("SESSION", "Focus session target reached. Completing.")
            stopFocusSessionUseCase(session.id, com.rebelfocus.core.domain.usecase.StopReason.Complete)
        } else {
            diagnosticLogger.info("SESSION", "Focus interval complete. Transitioning to BREAK.")
            transitionToBreakUseCase(session.id)
        }
    }

    private suspend fun handleBreakComplete(session: Session) {
        diagnosticLogger.info("SESSION", "Break complete. Transitioning to FOCUS.")
        transitionToActiveFocusUseCase(session.id)
    }

    private fun isTerminalState(state: SessionState): Boolean {
        return state == SessionState.Completed || 
               state == SessionState.Cancelled || 
               state == SessionState.EmergencyExit ||
               state == SessionState.Idle
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
