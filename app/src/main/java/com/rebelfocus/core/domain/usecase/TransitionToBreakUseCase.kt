package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.session.SessionEngine
import com.rebelfocus.core.model.Session
import javax.inject.Inject

/**
 * Transitions an active focus session into a break period.
 */
class TransitionToBreakUseCase @Inject constructor(
    private val sessionEngine: SessionEngine
) {
    suspend operator fun invoke(sessionId: String): Result<Session> = runCatching {
        sessionEngine.transitionToBreak(sessionId)
    }
}
