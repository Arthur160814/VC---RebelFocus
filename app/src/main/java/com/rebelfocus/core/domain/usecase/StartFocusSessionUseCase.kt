package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.domain.session.SessionEngine
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionType
import javax.inject.Inject

/**
 * Creates and starts a new focus session.
 *
 * Preconditions:
 * - No other session is currently active (ActiveFocus, Break, or Paused).
 * - Focus duration is positive.
 * - Pomodoro target is at least 1.
 */
class StartFocusSessionUseCase @Inject constructor(
    private val sessionEngine: SessionEngine,
    private val sessionRepository: SessionRepository
) {
    /**
     * @param profileId Optional profile to associate with the session.
     * @param type Session type (Pomodoro or FreeForm).
     * @param focusDurationMillis Duration of each focus interval in millis.
     * @param breakDurationMillis Duration of each break in millis (Pomodoro only).
     * @param pomodoroTarget Number of focus intervals (1 for FreeForm).
     * @return [Result.success] with the started session, or [Result.failure].
     */
    suspend operator fun invoke(
        profileId: String? = null,
        type: SessionType = SessionType.FreeForm,
        focusDurationMillis: Long,
        breakDurationMillis: Long = 0L,
        pomodoroTarget: Int = 1,
        isExtremeMode: Boolean = false,
        isUltimateMode: Boolean = false
    ): Result<Session> = runCatching {
        // Guard: no concurrent active sessions.
        val active = sessionRepository.getActiveSession()
        if (active != null) {
            error("Another session is already active (id=${active.id}, state=${active.state})")
        }

        // Temporary debugging log
        println("EXTREME_DEBUG: [UseCase] startFocusSessionUseCase called - isExtremeMode: $isExtremeMode, isUltimateMode: $isUltimateMode")

        sessionEngine.startSession(
            profileId = profileId,
            type = type,
            focusDurationMillis = focusDurationMillis,
            breakDurationMillis = breakDurationMillis,
            pomodoroTarget = pomodoroTarget,
            isExtremeMode = isExtremeMode,
            isUltimateMode = isUltimateMode
        )
    }
}
