package com.rebelfocus.core.domain.session

import com.rebelfocus.core.data.repository.AuditEventRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionType
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The central session orchestrator.
 *
 * Coordinates the [SessionStateMachine], [SessionTimingCalculator],
 * [SessionRepository], and [AuditEventRepository] to provide a
 * cohesive session lifecycle API.
 *
 * This class owns **no timers**. External components (services, UI)
 * call its methods to trigger transitions. The engine validates,
 * computes timing, persists, and logs — nothing more.
 */
@Singleton
class SessionEngine @Inject constructor(
    private val timeSource: TimeSource,
    private val sessionRepository: SessionRepository,
    private val auditEventRepository: AuditEventRepository,
    private val stateMachine: SessionStateMachine,
    private val timingCalculator: SessionTimingCalculator
) {

    // ── Observe ───────────────────────────────────────

    /**
     * Observe the currently active session (ActiveFocus, Break, or Paused).
     * Emits null when no session is active.
     */
    fun observeActiveSession(): Flow<Session?> = sessionRepository.observeActiveSession()

    // ── Start ─────────────────────────────────────────

    /**
     * Creates and starts a new focus session.
     *
     * @throws InvalidTransitionException if another session is already active
     *         (checked by the caller via use case).
     * @throws IllegalArgumentException if duration or target are invalid.
     */
    suspend fun startSession(
        profileId: String?,
        type: SessionType,
        focusDurationMillis: Long,
        breakDurationMillis: Long = 0L,
        pomodoroTarget: Int = 1,
        isExtremeMode: Boolean = false,
        isUltimateMode: Boolean = false
    ): Session {
        require(focusDurationMillis > 0) { "Focus duration must be positive" }
        require(pomodoroTarget >= 1) { "Pomodoro target must be at least 1" }

        val now = timeSource.elapsedRealtime()
        val wallClock = timeSource.currentTimeMillis()
        val sessionId = UUID.randomUUID().toString()

        val session = Session(
            id = sessionId,
            profileId = profileId,
            type = type,
            state = SessionState.ActiveFocus,
            startTimeElapsedRealtime = now,
            plannedDurationMillis = focusDurationMillis,
            elapsedAtPauseMillis = 0L,
            breakDurationMillis = breakDurationMillis,
            pomodoroCount = 0,
            pomodoroTarget = pomodoroTarget,
            isExtremeMode = isExtremeMode,
            isUltimateMode = isUltimateMode,
            createdAt = wallClock,
            updatedAt = wallClock,
            completedAt = null
        )

        sessionRepository.insert(session)
        
        // Temporary debugging log
        println("EXTREME_DEBUG: [Engine] Session Inserted - ID: ${session.id}, isExtreme: ${session.isExtremeMode}")

        logAudit(AuditEventType.SessionStarted, sessionId)
        return session
    }

    // ── Pause ─────────────────────────────────────────

    /**
     * Pauses the session, freezing focus time.
     *
     * Valid from: ActiveFocus, Break.
     */
    suspend fun pauseSession(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.Paused)

        val now = timeSource.elapsedRealtime()
        val frozen = timingCalculator.freezeFocusTime(session, now)
        val updated = frozen.copy(
            state = SessionState.Paused,
            updatedAt = timeSource.currentTimeMillis()
        )

        sessionRepository.update(updated)
        logAudit(AuditEventType.SessionPaused, sessionId)
        return updated
    }

    // ── Resume ────────────────────────────────────────

    /**
     * Resumes a paused session back to ActiveFocus.
     *
     * Valid from: Paused.
     */
    suspend fun resumeSession(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.ActiveFocus)

        val now = timeSource.elapsedRealtime()
        val resumed = timingCalculator.startTimingPhase(session, now)
        val updated = resumed.copy(
            state = SessionState.ActiveFocus,
            updatedAt = timeSource.currentTimeMillis()
        )

        sessionRepository.update(updated)
        logAudit(AuditEventType.SessionResumed, sessionId)
        return updated
    }

    // ── Transition to Break ───────────────────────────

    /**
     * Transitions from ActiveFocus to Break, incrementing pomodoroCount.
     *
     * Valid from: ActiveFocus.
     */
    suspend fun transitionToBreak(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.Break)

        val now = timeSource.elapsedRealtime()
        val frozen = timingCalculator.freezeFocusTime(session, now)
        val withBreakClock = timingCalculator.startTimingPhase(frozen, now)
        val updated = withBreakClock.copy(
            state = SessionState.Break,
            pomodoroCount = session.pomodoroCount + 1,
            updatedAt = timeSource.currentTimeMillis()
        )

        sessionRepository.update(updated)
        return updated
    }

    // ── Transition from Break to ActiveFocus ──────────

    /**
     * Transitions from Break back to ActiveFocus for the next pomodoro interval.
     *
     * Valid from: Break.
     */
    suspend fun transitionToActiveFocus(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.ActiveFocus)

        val now = timeSource.elapsedRealtime()
        val updated = timingCalculator.startTimingPhase(session, now).copy(
            state = SessionState.ActiveFocus,
            updatedAt = timeSource.currentTimeMillis()
        )

        sessionRepository.update(updated)
        return updated
    }

    // ── Complete ──────────────────────────────────────

    /**
     * Completes the session normally.
     *
     * Valid from: ActiveFocus, Break, Paused.
     */
    suspend fun completeSession(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.Completed)

        val now = timeSource.elapsedRealtime()
        val wallClock = timeSource.currentTimeMillis()
        val frozen = timingCalculator.freezeFocusTime(session, now)
        val updated = frozen.copy(
            state = SessionState.Completed,
            updatedAt = wallClock,
            completedAt = wallClock
        )

        sessionRepository.update(updated)
        logAudit(AuditEventType.SessionCompleted, sessionId)
        return updated
    }

    // ── Cancel ────────────────────────────────────────

    /**
     * Cancels the session.
     *
     * Valid from: Scheduled, ActiveFocus, Break, Paused.
     */
    suspend fun cancelSession(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.Cancelled)

        val now = timeSource.elapsedRealtime()
        val wallClock = timeSource.currentTimeMillis()
        val frozen = timingCalculator.freezeFocusTime(session, now)
        val updated = frozen.copy(
            state = SessionState.Cancelled,
            updatedAt = wallClock,
            completedAt = wallClock
        )

        sessionRepository.update(updated)
        logAudit(AuditEventType.SessionCancelled, sessionId)
        return updated
    }

    // ── Emergency Exit ────────────────────────────────

    /**
     * Terminates the session via emergency exit.
     *
     * Valid from: ActiveFocus, Break, Paused.
     */
    suspend fun emergencyExit(sessionId: String): Session {
        val session = requireSession(sessionId)
        stateMachine.validateTransition(session.state, SessionState.EmergencyExit)

        val now = timeSource.elapsedRealtime()
        val wallClock = timeSource.currentTimeMillis()
        val frozen = timingCalculator.freezeFocusTime(session, now)
        val updated = frozen.copy(
            state = SessionState.EmergencyExit,
            updatedAt = wallClock,
            completedAt = wallClock
        )

        sessionRepository.update(updated)
        logAudit(AuditEventType.SessionEmergencyExit, sessionId)
        return updated
    }

    // ── Restoration ───────────────────────────────────

    /**
     * Restores a session after process recreation.
     *
     * Handles device reboot detection: if the monotonic clock has reset,
     * adjusts the session's timing anchor to preserve accumulated time.
     *
     * @return The restored session, or null if no session is found.
     */
    suspend fun restoreSession(sessionId: String): Session? {
        val session = sessionRepository.getById(sessionId) ?: return null

        // Only restore non-terminal sessions.
        if (stateMachine.isTerminal(session.state)) return session

        val now = timeSource.elapsedRealtime()

        // Detect and handle reboot.
        if (timingCalculator.detectReboot(session, now)) {
            val adjusted = timingCalculator.adjustForReboot(session, now).copy(
                updatedAt = timeSource.currentTimeMillis()
            )
            sessionRepository.update(adjusted)
            return adjusted
        }

        return session
    }

    // ── Internal helpers ──────────────────────────────

    private suspend fun requireSession(sessionId: String): Session {
        return sessionRepository.getById(sessionId)
            ?: throw IllegalArgumentException("Session not found: $sessionId")
    }

    private suspend fun logAudit(eventType: AuditEventType, sessionId: String) {
        val event = AuditEvent(
            id = UUID.randomUUID().toString(),
            eventType = eventType,
            sessionId = sessionId,
            packageName = null,
            details = null,
            timestamp = timeSource.currentTimeMillis()
        )
        auditEventRepository.insert(event)
    }
}
