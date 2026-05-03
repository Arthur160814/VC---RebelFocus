package com.rebelfocus.services.foreground

import android.content.Context
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.SessionState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android-specific orchestration layer that observes the domain state (Active Session)
 * and manages the lifecycle of the [FocusSessionService].
 * 
 * This ensures the domain use-cases remain pure and completely decoupled from Android
 * Service APIs and Contexts.
 */
@Singleton
class FocusSessionOrchestrator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository,
    private val logger: com.rebelfocus.core.domain.logging.DiagnosticLogger
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isObserving = false
    private var lastObservedSessionId: String? = null

    fun startObserving() {
        if (isObserving) return
        isObserving = true
        logger.info("SYSTEM", "FocusSessionOrchestrator started observing")

        scope.launch {
            sessionRepository.observeActiveSession().collect { session ->
                if (session != null && !isTerminalState(session.state)) {
                    // Avoid repeatedly calling startService if already started for this session
                    if (session.id != lastObservedSessionId) {
                        logger.info("SESSION", "Transitioning to ACTIVE state: ${session.state} (ID: ${session.id})")
                        lastObservedSessionId = session.id
                        FocusSessionService.start(context)
                    }
                } else {
                    // Session is gone or terminal
                    if (lastObservedSessionId != null) {
                        logger.info("SESSION", "Session ended or terminal state reached")
                        lastObservedSessionId = null
                        FocusSessionService.stop(context)
                    }
                }
            }
        }
    }

    private fun isTerminalState(state: SessionState): Boolean {
        return state == SessionState.Completed ||
               state == SessionState.Cancelled ||
               state == SessionState.EmergencyExit ||
               state == SessionState.Idle
    }
}
