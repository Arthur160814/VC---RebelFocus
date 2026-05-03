package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.session.SessionEngine
import com.rebelfocus.core.model.Session
import javax.inject.Inject

/**
 * Stops a session — either as a normal completion or a cancellation.
 */
class StopFocusSessionUseCase @Inject constructor(
    private val sessionEngine: SessionEngine
) {
    /**
     * @param sessionId The ID of the session to stop.
     * @param reason Whether to complete or cancel.
     * @return [Result.success] with the terminal session, or [Result.failure].
     */
    suspend operator fun invoke(
        sessionId: String,
        reason: StopReason = StopReason.Complete
    ): Result<Session> = runCatching {
        when (reason) {
            StopReason.Complete -> sessionEngine.completeSession(sessionId)
            StopReason.Cancel -> sessionEngine.cancelSession(sessionId)
            StopReason.EmergencyExit -> sessionEngine.emergencyExit(sessionId)
        }
    }
}

enum class StopReason {
    Complete,
    Cancel,
    EmergencyExit
}
