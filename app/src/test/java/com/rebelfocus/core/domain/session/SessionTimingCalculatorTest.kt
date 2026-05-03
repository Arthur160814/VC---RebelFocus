package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests for [SessionTimingCalculator] — deterministic timing math.
 */
class SessionTimingCalculatorTest {

    private lateinit var calc: SessionTimingCalculator

    /** Helper to create a session with convenient defaults. */
    private fun session(
        state: SessionState = SessionState.ActiveFocus,
        startTime: Long? = 1000L,
        elapsedAtPause: Long = 0L,
        plannedDuration: Long = 25 * 60 * 1000L, // 25 min
        breakDuration: Long = 5 * 60 * 1000L, // 5 min
        pomodoroCount: Int = 0,
        pomodoroTarget: Int = 4
    ) = Session(
        id = "test-id",
        profileId = null,
        type = SessionType.Pomodoro,
        state = state,
        startTimeElapsedRealtime = startTime,
        plannedDurationMillis = plannedDuration,
        elapsedAtPauseMillis = elapsedAtPause,
        breakDurationMillis = breakDuration,
        pomodoroCount = pomodoroCount,
        pomodoroTarget = pomodoroTarget,
        createdAt = 0L,
        updatedAt = 0L,
        completedAt = null
    )

    @Before
    fun setUp() {
        calc = SessionTimingCalculator()
    }

    // ── Focus elapsed ─────────────────────────────────

    @Test
    fun `focus elapsed during ActiveFocus counts running time`() {
        val s = session(startTime = 1000L, elapsedAtPause = 0L)
        // 10 seconds have passed
        assertEquals(10_000L, calc.computeFocusElapsed(s, 11_000L))
    }

    @Test
    fun `focus elapsed during ActiveFocus includes accumulated pause time`() {
        val s = session(startTime = 5000L, elapsedAtPause = 3000L)
        // 2 seconds have passed in current phase + 3 seconds from before
        assertEquals(5000L, calc.computeFocusElapsed(s, 7000L))
    }

    @Test
    fun `focus elapsed during Paused is frozen at elapsedAtPause`() {
        val s = session(state = SessionState.Paused, startTime = null, elapsedAtPause = 7500L)
        assertEquals(7500L, calc.computeFocusElapsed(s, 999_999L))
    }

    @Test
    fun `focus elapsed during Break is frozen`() {
        val s = session(state = SessionState.Break, startTime = 2000L, elapsedAtPause = 10_000L)
        assertEquals(10_000L, calc.computeFocusElapsed(s, 5000L))
    }

    @Test
    fun `focus elapsed never goes negative`() {
        // Edge case: now < startTime (shouldn't happen normally, but coerce)
        val s = session(startTime = 5000L, elapsedAtPause = 0L)
        assertEquals(0L, calc.computeFocusElapsed(s, 3000L))
    }

    // ── Break elapsed ─────────────────────────────────

    @Test
    fun `break elapsed during Break counts from startTime`() {
        val s = session(state = SessionState.Break, startTime = 1000L)
        assertEquals(4000L, calc.computeBreakElapsed(s, 5000L))
    }

    @Test
    fun `break elapsed is 0 when not in Break`() {
        val s = session(state = SessionState.ActiveFocus, startTime = 1000L)
        assertEquals(0L, calc.computeBreakElapsed(s, 5000L))
    }

    // ── Focus remaining ───────────────────────────────

    @Test
    fun `focus remaining decreases during active focus`() {
        val s = session(startTime = 0L, plannedDuration = 10_000L, pomodoroTarget = 1)
        assertEquals(7000L, calc.computeFocusRemaining(s, 3000L))
    }

    @Test
    fun `focus remaining is 0 when interval exceeded`() {
        val s = session(startTime = 0L, plannedDuration = 10_000L, pomodoroTarget = 1)
        assertEquals(0L, calc.computeFocusRemaining(s, 15_000L))
    }

    @Test
    fun `focus remaining accounts for pomodoro count`() {
        // 2nd interval (pomodoroCount=1 means 1 interval completed)
        val s = session(
            startTime = 50_000L,
            elapsedAtPause = 25 * 60 * 1000L, // 25 min from 1st interval
            pomodoroCount = 1,
            plannedDuration = 25 * 60 * 1000L,
            pomodoroTarget = 4
        )
        // 10 seconds into 2nd interval
        val remaining = calc.computeFocusRemaining(s, 60_000L)
        val expectedElapsedThisInterval = (25 * 60 * 1000L + 10_000L) - (1L * 25 * 60 * 1000L)
        val expectedRemaining = 25 * 60 * 1000L - expectedElapsedThisInterval
        assertEquals(expectedRemaining, remaining)
    }

    // ── Break remaining ───────────────────────────────

    @Test
    fun `break remaining decreases during break`() {
        val s = session(state = SessionState.Break, startTime = 0L, breakDuration = 5000L)
        assertEquals(3000L, calc.computeBreakRemaining(s, 2000L))
    }

    @Test
    fun `break remaining is 0 when not in break`() {
        val s = session(state = SessionState.ActiveFocus)
        assertEquals(0L, calc.computeBreakRemaining(s, 5000L))
    }

    // ── Completion checks ─────────────────────────────

    @Test
    fun `session fully complete when total focus exceeds all intervals`() {
        val s = session(
            startTime = 0L,
            plannedDuration = 1000L,
            pomodoroTarget = 3,
            pomodoroCount = 2,
            elapsedAtPause = 2000L
        )
        // 3 intervals × 1000ms = 3000ms total needed
        // Already have 2000ms accumulated + 1000ms running = 3000ms
        assertTrue(calc.isSessionFullyComplete(s, 1000L))
    }

    @Test
    fun `session not fully complete when focus is still short`() {
        val s = session(
            startTime = 0L,
            plannedDuration = 1000L,
            pomodoroTarget = 3
        )
        assertFalse(calc.isSessionFullyComplete(s, 500L))
    }

    // ── Reboot detection ──────────────────────────────

    @Test
    fun `detects reboot when now is less than startTime`() {
        val s = session(startTime = 100_000L)
        assertTrue(calc.detectReboot(s, 5000L))
    }

    @Test
    fun `no reboot when now is greater than startTime`() {
        val s = session(startTime = 1000L)
        assertFalse(calc.detectReboot(s, 5000L))
    }

    @Test
    fun `no reboot when startTime is null`() {
        val s = session(startTime = null)
        assertFalse(calc.detectReboot(s, 5000L))
    }

    @Test
    fun `adjustForReboot resets startTime to now`() {
        val s = session(startTime = 100_000L, elapsedAtPause = 5000L)
        val adjusted = calc.adjustForReboot(s, 2000L)
        assertEquals(2000L, adjusted.startTimeElapsedRealtime)
        // Accumulated time preserved
        assertEquals(5000L, adjusted.elapsedAtPauseMillis)
    }

    // ── Freeze / start timing phase ───────────────────

    @Test
    fun `freezeFocusTime snapshots total and clears startTime`() {
        val s = session(startTime = 1000L, elapsedAtPause = 3000L)
        val frozen = calc.freezeFocusTime(s, 6000L)
        // Total = 3000 + (6000 - 1000) = 8000
        assertEquals(8000L, frozen.elapsedAtPauseMillis)
        assertNull(frozen.startTimeElapsedRealtime)
    }

    @Test
    fun `startTimingPhase sets startTime to now`() {
        val s = session(startTime = null)
        val started = calc.startTimingPhase(s, 42_000L)
        assertEquals(42_000L, started.startTimeElapsedRealtime)
    }
}
