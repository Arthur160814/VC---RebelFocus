package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState

/**
 * Pure-Kotlin timing calculations for focus sessions.
 *
 * All methods are deterministic — they take the current monotonic time
 * as a parameter rather than reading it from the system clock.
 *
 * ## Timing model
 *
 * - [Session.startTimeElapsedRealtime]: monotonic timestamp when the current
 *   active phase (focus or break) started.
 * - [Session.elapsedAtPauseMillis]: accumulated focus time from all previous
 *   active-focus periods prior to the current one.
 *
 * During **ActiveFocus**:
 * ```
 * totalFocusElapsed = elapsedAtPauseMillis + (now - startTimeElapsedRealtime)
 * ```
 *
 * During **Break**:
 * ```
 * totalFocusElapsed = elapsedAtPauseMillis  (frozen — not counting focus time)
 * breakElapsed      = now - startTimeElapsedRealtime
 * ```
 *
 * During **Paused**:
 * ```
 * totalFocusElapsed = elapsedAtPauseMillis  (frozen)
 * ```
 */
class SessionTimingCalculator {

    /**
     * Computes the total focus time elapsed for a session.
     *
     * @param session The session to compute for.
     * @param nowElapsedRealtime The current monotonic time in millis.
     * @return Total focus time in milliseconds.
     */
    fun computeFocusElapsed(session: Session, nowElapsedRealtime: Long): Long {
        return when (session.state) {
            SessionState.ActiveFocus -> {
                val start = session.startTimeElapsedRealtime ?: return session.elapsedAtPauseMillis
                val currentPhaseElapsed = (nowElapsedRealtime - start).coerceAtLeast(0)
                session.elapsedAtPauseMillis + currentPhaseElapsed
            }
            // During Break, Paused, or terminal states focus time is frozen.
            else -> session.elapsedAtPauseMillis
        }
    }

    /**
     * Computes elapsed break time (only meaningful during [SessionState.Break]).
     *
     * @return Break time in milliseconds, or 0 if not in a break.
     */
    fun computeBreakElapsed(session: Session, nowElapsedRealtime: Long): Long {
        if (session.state != SessionState.Break) return 0L
        val start = session.startTimeElapsedRealtime ?: return 0L
        return (nowElapsedRealtime - start).coerceAtLeast(0)
    }

    /**
     * Computes remaining focus time in the current pomodoro interval.
     *
     * @return Remaining millis, or 0 if the interval is already exceeded.
     */
    fun computeFocusRemaining(session: Session, nowElapsedRealtime: Long): Long {
        val focusPerInterval = session.plannedDurationMillis
        val focusElapsed = computeFocusElapsed(session, nowElapsedRealtime)
        // For Pomodoro, focus time resets each interval.
        // Elapsed in *current* interval = focusElapsed - (pomodoroCount * focusPerInterval)
        val elapsedThisInterval = focusElapsed - (session.pomodoroCount.toLong() * focusPerInterval)
        return (focusPerInterval - elapsedThisInterval).coerceAtLeast(0)
    }

    /**
     * Computes remaining break time in the current break period.
     *
     * @return Remaining millis, or 0 if not in a break.
     */
    fun computeBreakRemaining(session: Session, nowElapsedRealtime: Long): Long {
        if (session.state != SessionState.Break) return 0L
        val breakElapsed = computeBreakElapsed(session, nowElapsedRealtime)
        return (session.breakDurationMillis - breakElapsed).coerceAtLeast(0)
    }

    /**
     * Returns `true` if the current focus interval has reached its planned duration.
     */
    fun isFocusIntervalComplete(session: Session, nowElapsedRealtime: Long): Boolean {
        return computeFocusRemaining(session, nowElapsedRealtime) == 0L
    }

    /**
     * Returns `true` if the current break has reached its planned duration.
     */
    fun isBreakComplete(session: Session, nowElapsedRealtime: Long): Boolean {
        if (session.state != SessionState.Break) return false
        return computeBreakRemaining(session, nowElapsedRealtime) == 0L
    }

    /**
     * Returns `true` if all pomodoro intervals are complete.
     * For FreeForm sessions, this is true when the single focus period ends.
     */
    fun isSessionFullyComplete(session: Session, nowElapsedRealtime: Long): Boolean {
        val focusElapsed = computeFocusElapsed(session, nowElapsedRealtime)
        val totalPlannedFocus = session.plannedDurationMillis * session.pomodoroTarget
        return focusElapsed >= totalPlannedFocus
    }

    /**
     * Detects whether a device reboot has occurred since the session started.
     *
     * `elapsedRealtime()` resets to 0 on reboot, so if the current time is
     * less than the stored start time, a reboot happened.
     */
    fun detectReboot(session: Session, nowElapsedRealtime: Long): Boolean {
        val start = session.startTimeElapsedRealtime ?: return false
        return nowElapsedRealtime < start
    }

    /**
     * Adjusts a session's timing after a detected reboot.
     *
     * Since the monotonic clock reset, we treat the reboot as a pause boundary:
     * the accumulated time is already stored in [Session.elapsedAtPauseMillis],
     * and we reset [Session.startTimeElapsedRealtime] to the current time.
     */
    fun adjustForReboot(session: Session, nowElapsedRealtime: Long): Session {
        return session.copy(
            startTimeElapsedRealtime = nowElapsedRealtime
            // elapsedAtPauseMillis already holds the accumulated focus time up to the reboot.
        )
    }

    /**
     * Snapshot the current focus time into [Session.elapsedAtPauseMillis]
     * and clear the running clock. Used before pausing or entering a break.
     */
    fun freezeFocusTime(session: Session, nowElapsedRealtime: Long): Session {
        val totalFocus = computeFocusElapsed(session, nowElapsedRealtime)
        return session.copy(
            elapsedAtPauseMillis = totalFocus,
            startTimeElapsedRealtime = null
        )
    }

    /**
     * Start a new timing phase by setting [Session.startTimeElapsedRealtime] to now.
     * Used when entering ActiveFocus or Break.
     */
    fun startTimingPhase(session: Session, nowElapsedRealtime: Long): Session {
        return session.copy(
            startTimeElapsedRealtime = nowElapsedRealtime
        )
    }
}
