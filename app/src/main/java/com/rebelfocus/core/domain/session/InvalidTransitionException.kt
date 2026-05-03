package com.rebelfocus.core.domain.session

import com.rebelfocus.core.model.SessionState

/**
 * Thrown when a state transition is not valid according to the
 * [SessionStateMachine] transition table.
 */
class InvalidTransitionException(
    val from: SessionState,
    val to: SessionState
) : IllegalStateException(
    "Invalid session transition: $from → $to"
)
