package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.session.SessionEngine
import com.rebelfocus.core.model.Session
import javax.inject.Inject

/**
 * Resumes a paused session, restarting the focus timer.
 */
class ResumeSessionUseCase @Inject constructor(
    private val sessionEngine: SessionEngine
) {
    /**
     * @param sessionId The ID of the session to resume.
     * @return [Result.success] with the resumed session, or [Result.failure].
     */
    suspend operator fun invoke(sessionId: String): Result<Session> = runCatching {
        sessionEngine.resumeSession(sessionId)
    }
}
