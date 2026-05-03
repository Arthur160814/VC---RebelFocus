package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionState.*

/**
 * Pure-Kotlin state machine that defines valid session transitions.
 *
 * Every state transition in the app must pass through [validateTransition].
 * Invalid transitions throw [InvalidTransitionException].
 *
 * ```
 * Idle ──────────┬──→ ActiveFocus
 *                └──→ Scheduled ──→ ActiveFocus
 *                                    │
 *          ┌─────────────────────────┤
 *          ↓                         ↓
 *        Break ←──────────────→ Paused
 *          │                      │
 *          └──→ ActiveFocus ←─────┘
 *
 * Any active state → Cancelled / EmergencyExit / Completed
 * Terminal states (Completed / Cancelled / EmergencyExit) → (no outward transitions)
 * ```
 */
class SessionStateMachine {

    /**
     * Map of each state to the set of states it can transition to.
     */
    private val transitions: Map<SessionState, Set<SessionState>> = mapOf(
        Idle to setOf(Scheduled, ActiveFocus),
        Scheduled to setOf(ActiveFocus, Cancelled),
        ActiveFocus to setOf(Paused, Break, Completed, Cancelled, EmergencyExit),
        Break to setOf(ActiveFocus, Paused, Completed, Cancelled, EmergencyExit),
        Paused to setOf(ActiveFocus, Break, Completed, Cancelled, EmergencyExit),
        Completed to emptySet(),
        Cancelled to emptySet(),
        EmergencyExit to emptySet()
    )

    /**
     * Returns `true` if a transition from [from] to [to] is valid.
     */
    fun isValidTransition(from: SessionState, to: SessionState): Boolean {
        return transitions[from]?.contains(to) == true
    }

    /**
     * Validates a transition. Throws [InvalidTransitionException] if invalid.
     */
    fun validateTransition(from: SessionState, to: SessionState) {
        if (!isValidTransition(from, to)) {
            throw InvalidTransitionException(from, to)
        }
    }

    /**
     * Returns the set of valid target states from [state].
     */
    fun validTargetsFrom(state: SessionState): Set<SessionState> {
        return transitions[state] ?: emptySet()
    }

    /**
     * Returns `true` if [state] is a terminal state with no outgoing transitions.
     */
    fun isTerminal(state: SessionState): Boolean {
        return validTargetsFrom(state).isEmpty()
    }
}
