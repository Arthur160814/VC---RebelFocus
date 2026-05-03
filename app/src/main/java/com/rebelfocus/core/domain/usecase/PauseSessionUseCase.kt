package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.session.SessionEngine
import com.rebelfocus.core.model.Session
import javax.inject.Inject

/**
 * Pauses an active focus session, freezing the focus timer.
 */
class PauseSessionUseCase @Inject constructor(
    private val sessionEngine: SessionEngine
) {
    /**
     * @param sessionId The ID of the session to pause.
     * @return [Result.success] with the paused session, or [Result.failure].
     */
    suspend operator fun invoke(sessionId: String): Result<Session> = runCatching {
        sessionEngine.pauseSession(sessionId)
    }
}
