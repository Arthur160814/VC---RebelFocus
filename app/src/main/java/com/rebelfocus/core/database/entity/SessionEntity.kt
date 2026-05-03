package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for persisting focus sessions.
 *
 * Timing uses monotonic elapsedRealtime for accuracy across process recreation.
 * Wall-clock times are stored for display and audit purposes only.
 */
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "profile_id")
    val profileId: String?,
    @ColumnInfo(name = "session_type")
    val sessionType: String,
    val state: String,
    @ColumnInfo(name = "start_time_elapsed_realtime")
    val startTimeElapsedRealtime: Long?,
    @ColumnInfo(name = "planned_duration_millis")
    val plannedDurationMillis: Long,
    @ColumnInfo(name = "elapsed_at_pause_millis")
    val elapsedAtPauseMillis: Long,
    @ColumnInfo(name = "break_duration_millis")
    val breakDurationMillis: Long,
    @ColumnInfo(name = "pomodoro_count")
    val pomodoroCount: Int,
    @ColumnInfo(name = "pomodoro_target")
    val pomodoroTarget: Int,
    @ColumnInfo(name = "is_extreme_mode")
    val isExtremeMode: Boolean = false,
    @ColumnInfo(name = "is_ultimate_mode")
    val isUltimateMode: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "completed_at")
    val completedAt: Long?
)
