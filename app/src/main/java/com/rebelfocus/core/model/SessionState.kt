package com.rebelfocus.core.model

/**
 * Represents the possible states of a focus session.
 * This is the domain-level enum — not tied to Room.
 */
enum class SessionState {
    /** No active session. */
    Idle,
    /** Session is scheduled to start in the future. */
    Scheduled,
    /** Session is actively running and tracking focus time. */
    ActiveFocus,
    /** Session is in a break period (Pomodoro). */
    Break,
    /** Session is temporarily paused by the user. */
    Paused,
    /** Session completed its full planned duration. */
    Completed,
    /** Session was manually cancelled by the user. */
    Cancelled,
    /** Session was ended via emergency exit. */
    EmergencyExit
}
