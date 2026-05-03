package com.rebelfocus.core.model

/**
 * Domain model for a focus session.
 *
 * The session engine (Phase 2) will use this as its primary data type.
 * Timing is based on monotonic clock — [startTimeElapsedRealtime] stores the
 * elapsedRealtime() value when the session entered ActiveFocus.
 */
data class Session(
    val id: String,
    val profileId: String?,
    val type: SessionType,
    val state: SessionState,
    /** elapsedRealtime() timestamp when session started focusing. Null if not yet active. */
    val startTimeElapsedRealtime: Long?,
    /** Intended focus duration in milliseconds. */
    val plannedDurationMillis: Long,
    /** Accumulated focus time in millis at the moment of the last pause. */
    val elapsedAtPauseMillis: Long,
    /** Break duration in millis (relevant for Pomodoro). */
    val breakDurationMillis: Long,
    /** Number of completed pomodoro intervals in this session. */
    val pomodoroCount: Int,
    /** Target number of pomodoro intervals. */
    val pomodoroTarget: Int,
    val isExtremeMode: Boolean = false,
    val isUltimateMode: Boolean = false,
    /** Wall-clock timestamp when the session record was created. */
    val createdAt: Long,
    /** Wall-clock timestamp of the last update. */
    val updatedAt: Long,
    /** Wall-clock timestamp when the session ended. Null if still active. */
    val completedAt: Long?
)
