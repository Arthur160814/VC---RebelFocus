package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.AuditEventType
import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Integration-style tests for [SessionEngine] using fake repos and a fake clock.
 *
 * These tests verify the full lifecycle: start → pause → resume → complete,
 * as well as error paths and reboot recovery.
 */
class SessionEngineTest {

    private lateinit var timeSource: FakeTimeSource
    private lateinit var sessionRepo: FakeSessionRepository
    private lateinit var auditRepo: FakeAuditEventRepository
    private lateinit var engine: SessionEngine

    @Before
    fun setUp() {
        timeSource = FakeTimeSource(elapsedRealtimeMillis = 10_000L)
        sessionRepo = FakeSessionRepository()
        auditRepo = FakeAuditEventRepository()
        engine = SessionEngine(
            timeSource = timeSource,
            sessionRepository = sessionRepo,
            auditEventRepository = auditRepo,
            stateMachine = SessionStateMachine(),
            timingCalculator = SessionTimingCalculator()
        )
    }

    // ── Start ─────────────────────────────────────────

    @Test
    fun `startSession creates an ActiveFocus session`() = runTest {
        val session = engine.startSession(
            profileId = "p1",
            type = SessionType.Pomodoro,
            focusDurationMillis = 25 * 60_000L,
            breakDurationMillis = 5 * 60_000L,
            pomodoroTarget = 4
        )

        assertEquals(SessionState.ActiveFocus, session.state)
        assertEquals(SessionType.Pomodoro, session.type)
        assertEquals("p1", session.profileId)
        assertEquals(10_000L, session.startTimeElapsedRealtime)
        assertEquals(0L, session.elapsedAtPauseMillis)
        assertEquals(0, session.pomodoroCount)
        assertEquals(4, session.pomodoroTarget)
        assertNotNull(sessionRepo.getById(session.id))
    }

    @Test
    fun `startSession logs SessionStarted audit event`() = runTest {
        val session = engine.startSession(
            profileId = null,
            type = SessionType.FreeForm,
            focusDurationMillis = 60_000L
        )

        val events = auditRepo.allEvents()
        assertEquals(1, events.size)
        assertEquals(AuditEventType.SessionStarted, events[0].eventType)
        assertEquals(session.id, events[0].sessionId)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `startSession rejects zero duration`() = runTest {
        engine.startSession(
            profileId = null,
            type = SessionType.FreeForm,
            focusDurationMillis = 0L
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `startSession rejects zero pomodoro target`() = runTest {
        engine.startSession(
            profileId = null,
            type = SessionType.Pomodoro,
            focusDurationMillis = 60_000L,
            pomodoroTarget = 0
        )
    }

    // ── Pause ─────────────────────────────────────────

    @Test
    fun `pauseSession freezes focus time and transitions to Paused`() = runTest {
        val started = engine.startSession(
            profileId = null,
            type = SessionType.FreeForm,
            focusDurationMillis = 60_000L
        )

        timeSource.advanceBy(5000L) // 5 seconds of focus
        val paused = engine.pauseSession(started.id)

        assertEquals(SessionState.Paused, paused.state)
        assertEquals(5000L, paused.elapsedAtPauseMillis)
        assertNull(paused.startTimeElapsedRealtime)
    }

    @Test
    fun `pauseSession logs SessionPaused audit event`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(1000L)
        engine.pauseSession(started.id)

        val pauseEvents = auditRepo.allEvents().filter { it.eventType == AuditEventType.SessionPaused }
        assertEquals(1, pauseEvents.size)
    }

    // ── Resume ────────────────────────────────────────

    @Test
    fun `resumeSession starts a new timing phase without losing accumulated time`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(5000L)
        engine.pauseSession(started.id)

        timeSource.advanceBy(3000L) // 3 seconds of pause (should not count)
        val resumed = engine.resumeSession(started.id)

        assertEquals(SessionState.ActiveFocus, resumed.state)
        assertEquals(5000L, resumed.elapsedAtPauseMillis) // unchanged
        assertEquals(18_000L, resumed.startTimeElapsedRealtime) // 10k + 5k + 3k
    }

    @Test
    fun `pause time does not count as focus time`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(5000L) // 5s focus
        engine.pauseSession(started.id)

        timeSource.advanceBy(100_000L) // 100s pause
        val resumed = engine.resumeSession(started.id)

        timeSource.advanceBy(3000L) // 3s more focus
        // Total focus should be 5s + 3s = 8s, NOT 108s
        val calc = SessionTimingCalculator()
        val totalFocus = calc.computeFocusElapsed(
            sessionRepo.getById(resumed.id)!!,
            timeSource.elapsedRealtime()
        )
        assertEquals(8000L, totalFocus)
    }

    // ── Complete ──────────────────────────────────────

    @Test
    fun `completeSession transitions to Completed with final timestamp`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(60_000L)
        val completed = engine.completeSession(started.id)

        assertEquals(SessionState.Completed, completed.state)
        assertNotNull(completed.completedAt)
        assertEquals(60_000L, completed.elapsedAtPauseMillis)
    }

    // ── Cancel ────────────────────────────────────────

    @Test
    fun `cancelSession transitions to Cancelled`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(10_000L)
        val cancelled = engine.cancelSession(started.id)

        assertEquals(SessionState.Cancelled, cancelled.state)
        assertNotNull(cancelled.completedAt)
    }

    @Test
    fun `cancelSession logs SessionCancelled audit event`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        engine.cancelSession(started.id)

        val cancelEvents = auditRepo.allEvents().filter { it.eventType == AuditEventType.SessionCancelled }
        assertEquals(1, cancelEvents.size)
    }

    // ── Emergency exit ────────────────────────────────

    @Test
    fun `emergencyExit transitions to EmergencyExit`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        val exited = engine.emergencyExit(started.id)

        assertEquals(SessionState.EmergencyExit, exited.state)
        assertNotNull(exited.completedAt)
    }

    @Test
    fun `emergencyExit logs SessionEmergencyExit audit event`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        engine.emergencyExit(started.id)

        val exitEvents = auditRepo.allEvents().filter { it.eventType == AuditEventType.SessionEmergencyExit }
        assertEquals(1, exitEvents.size)
    }

    // ── Invalid transitions ───────────────────────────

    @Test(expected = InvalidTransitionException::class)
    fun `cannot pause an already paused session`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        engine.pauseSession(started.id)
        engine.pauseSession(started.id) // Paused → Paused is invalid
    }

    @Test(expected = InvalidTransitionException::class)
    fun `cannot resume a non-paused session`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        // ActiveFocus → ActiveFocus should fail (self-transition not in valid set)
        engine.resumeSession(started.id)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `cannot complete a completed session`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        engine.completeSession(started.id)
        engine.completeSession(started.id) // Completed → Completed is invalid
    }

    // ── Pomodoro transitions ──────────────────────────

    @Test
    fun `transitionToBreak increments pomodoroCount`() = runTest {
        val started = engine.startSession(
            profileId = null,
            type = SessionType.Pomodoro,
            focusDurationMillis = 25 * 60_000L,
            breakDurationMillis = 5 * 60_000L,
            pomodoroTarget = 4
        )

        timeSource.advanceBy(25 * 60_000L)
        val inBreak = engine.transitionToBreak(started.id)

        assertEquals(SessionState.Break, inBreak.state)
        assertEquals(1, inBreak.pomodoroCount)
        assertNotNull(inBreak.startTimeElapsedRealtime) // break clock started
    }

    @Test
    fun `transitionToActiveFocus after Break resumes focus`() = runTest {
        val started = engine.startSession(
            profileId = null,
            type = SessionType.Pomodoro,
            focusDurationMillis = 25 * 60_000L,
            breakDurationMillis = 5 * 60_000L,
            pomodoroTarget = 4
        )

        timeSource.advanceBy(25 * 60_000L)
        val inBreak = engine.transitionToBreak(started.id)
        timeSource.advanceBy(5 * 60_000L)
        val backToFocus = engine.transitionToActiveFocus(inBreak.id)

        assertEquals(SessionState.ActiveFocus, backToFocus.state)
        assertEquals(1, backToFocus.pomodoroCount) // still 1
    }

    // ── Restoration ───────────────────────────────────

    @Test
    fun `restoreSession returns session with adjusted timing after reboot`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        timeSource.advanceBy(10_000L)

        // Simulate reboot: reset monotonic clock
        timeSource.elapsedRealtimeMillis = 500L // much less than startTimeElapsedRealtime

        val restored = engine.restoreSession(started.id)

        assertNotNull(restored)
        assertEquals(500L, restored!!.startTimeElapsedRealtime)
        // Accumulated time should be preserved (was 0 + no freeze on active)
    }

    @Test
    fun `restoreSession returns terminal session unchanged`() = runTest {
        val started = engine.startSession(null, SessionType.FreeForm, 60_000L)
        engine.completeSession(started.id)

        val restored = engine.restoreSession(started.id)
        assertEquals(SessionState.Completed, restored?.state)
    }

    @Test
    fun `restoreSession returns null for unknown session`() = runTest {
        val result = engine.restoreSession("nonexistent-id")
        assertNull(result)
    }

    // ── Session not found ─────────────────────────────

    @Test(expected = IllegalArgumentException::class)
    fun `pauseSession throws for nonexistent session`() = runTest {
        engine.pauseSession("does-not-exist")
    }

    // ── Full lifecycle ────────────────────────────────

    @Test
    fun `full Pomodoro lifecycle with 2 intervals`() = runTest {
        val session = engine.startSession(
            profileId = null,
            type = SessionType.Pomodoro,
            focusDurationMillis = 1000L,
            breakDurationMillis = 500L,
            pomodoroTarget = 2
        )

        // 1st focus interval
        timeSource.advanceBy(1000L)
        val break1 = engine.transitionToBreak(session.id)
        assertEquals(1, break1.pomodoroCount)

        // 1st break
        timeSource.advanceBy(500L)
        val focus2 = engine.transitionToActiveFocus(session.id)
        assertEquals(SessionState.ActiveFocus, focus2.state)

        // 2nd focus interval
        timeSource.advanceBy(1000L)
        val break2 = engine.transitionToBreak(session.id)
        assertEquals(2, break2.pomodoroCount)

        // Complete after final interval
        val completed = engine.completeSession(session.id)
        assertEquals(SessionState.Completed, completed.state)

        // Verify total focus time = 2 × 1000ms = 2000ms
        assertEquals(2000L, completed.elapsedAtPauseMillis)

        // Verify audit trail
        val events = auditRepo.allEvents()
        assertTrue(events.any { it.eventType == AuditEventType.SessionStarted })
        assertTrue(events.any { it.eventType == AuditEventType.SessionCompleted })
    }

    @Test
    fun `full lifecycle with pause in the middle`() = runTest {
        val session = engine.startSession(null, SessionType.FreeForm, 60_000L)

        // Focus for 10s
        timeSource.advanceBy(10_000L)
        engine.pauseSession(session.id)

        // Wait 30s while paused
        timeSource.advanceBy(30_000L)
        engine.resumeSession(session.id)

        // Focus for 5s more
        timeSource.advanceBy(5_000L)
        val completed = engine.completeSession(session.id)

        // Total focus = 10s + 5s = 15s (NOT 45s)
        assertEquals(15_000L, completed.elapsedAtPauseMillis)
    }
}
