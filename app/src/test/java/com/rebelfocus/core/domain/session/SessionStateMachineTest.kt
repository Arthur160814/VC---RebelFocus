package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionState.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Tests for [SessionStateMachine] — validates every valid transition
 * and explicitly rejects every invalid one.
 */
class SessionStateMachineTest {

    private lateinit var sm: SessionStateMachine

    @Before
    fun setUp() {
        sm = SessionStateMachine()
    }

    // ── Valid transitions ──────────────────────────────

    @Test
    fun `Idle to ActiveFocus is valid`() {
        assertTrue(sm.isValidTransition(Idle, ActiveFocus))
    }

    @Test
    fun `Idle to Scheduled is valid`() {
        assertTrue(sm.isValidTransition(Idle, Scheduled))
    }

    @Test
    fun `Scheduled to ActiveFocus is valid`() {
        assertTrue(sm.isValidTransition(Scheduled, ActiveFocus))
    }

    @Test
    fun `Scheduled to Cancelled is valid`() {
        assertTrue(sm.isValidTransition(Scheduled, Cancelled))
    }

    @Test
    fun `ActiveFocus to Paused is valid`() {
        assertTrue(sm.isValidTransition(ActiveFocus, Paused))
    }

    @Test
    fun `ActiveFocus to Break is valid`() {
        assertTrue(sm.isValidTransition(ActiveFocus, Break))
    }

    @Test
    fun `ActiveFocus to Completed is valid`() {
        assertTrue(sm.isValidTransition(ActiveFocus, Completed))
    }

    @Test
    fun `ActiveFocus to Cancelled is valid`() {
        assertTrue(sm.isValidTransition(ActiveFocus, Cancelled))
    }

    @Test
    fun `ActiveFocus to EmergencyExit is valid`() {
        assertTrue(sm.isValidTransition(ActiveFocus, EmergencyExit))
    }

    @Test
    fun `Break to ActiveFocus is valid`() {
        assertTrue(sm.isValidTransition(Break, ActiveFocus))
    }

    @Test
    fun `Break to Paused is valid`() {
        assertTrue(sm.isValidTransition(Break, Paused))
    }

    @Test
    fun `Break to Completed is valid`() {
        assertTrue(sm.isValidTransition(Break, Completed))
    }

    @Test
    fun `Break to Cancelled is valid`() {
        assertTrue(sm.isValidTransition(Break, Cancelled))
    }

    @Test
    fun `Break to EmergencyExit is valid`() {
        assertTrue(sm.isValidTransition(Break, EmergencyExit))
    }

    @Test
    fun `Paused to ActiveFocus is valid`() {
        assertTrue(sm.isValidTransition(Paused, ActiveFocus))
    }

    @Test
    fun `Paused to Break is valid`() {
        assertTrue(sm.isValidTransition(Paused, Break))
    }

    @Test
    fun `Paused to Completed is valid`() {
        assertTrue(sm.isValidTransition(Paused, Completed))
    }

    @Test
    fun `Paused to Cancelled is valid`() {
        assertTrue(sm.isValidTransition(Paused, Cancelled))
    }

    @Test
    fun `Paused to EmergencyExit is valid`() {
        assertTrue(sm.isValidTransition(Paused, EmergencyExit))
    }

    // ── Invalid transitions ───────────────────────────

    @Test(expected = InvalidTransitionException::class)
    fun `Idle to Paused throws`() {
        sm.validateTransition(Idle, Paused)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Idle to Break throws`() {
        sm.validateTransition(Idle, Break)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Idle to Completed throws`() {
        sm.validateTransition(Idle, Completed)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Idle to Cancelled throws`() {
        sm.validateTransition(Idle, Cancelled)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Scheduled to Paused throws`() {
        sm.validateTransition(Scheduled, Paused)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Scheduled to Break throws`() {
        sm.validateTransition(Scheduled, Break)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Completed to anything throws`() {
        sm.validateTransition(Completed, ActiveFocus)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `Cancelled to anything throws`() {
        sm.validateTransition(Cancelled, ActiveFocus)
    }

    @Test(expected = InvalidTransitionException::class)
    fun `EmergencyExit to anything throws`() {
        sm.validateTransition(EmergencyExit, ActiveFocus)
    }

    // ── Terminal state checks ─────────────────────────

    @Test
    fun `Completed is terminal`() {
        assertTrue(sm.isTerminal(Completed))
    }

    @Test
    fun `Cancelled is terminal`() {
        assertTrue(sm.isTerminal(Cancelled))
    }

    @Test
    fun `EmergencyExit is terminal`() {
        assertTrue(sm.isTerminal(EmergencyExit))
    }

    @Test
    fun `ActiveFocus is not terminal`() {
        assertFalse(sm.isTerminal(ActiveFocus))
    }

    @Test
    fun `Idle is not terminal`() {
        assertFalse(sm.isTerminal(Idle))
    }

    // ── Exhaustive invalid-from-terminal check ────────

    @Test
    fun `no transitions out of terminal states`() {
        val terminalStates = listOf(Completed, Cancelled, EmergencyExit)
        for (terminal in terminalStates) {
            for (target in SessionState.entries) {
                assertFalse(
                    "Expected $terminal → $target to be invalid",
                    sm.isValidTransition(terminal, target)
                )
            }
        }
    }
}
