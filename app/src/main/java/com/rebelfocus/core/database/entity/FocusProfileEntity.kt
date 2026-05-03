package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for focus profiles.
 *
 * Blocked apps are linked via [ProfileBlockedAppCrossRef].
 */
@Entity(tableName = "focus_profiles")
data class FocusProfileEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "is_default")
    val isDefault: Boolean,
    @ColumnInfo(name = "session_type")
    val sessionType: String,
    @ColumnInfo(name = "is_extreme_mode")
    val isExtremeMode: Boolean = false,
    @ColumnInfo(name = "is_ultimate_mode")
    val isUltimateMode: Boolean = false,
    @ColumnInfo(name = "focus_duration_millis")
    val focusDurationMillis: Long,
    @ColumnInfo(name = "break_duration_millis")
    val breakDurationMillis: Long,
    @ColumnInfo(name = "pomodoro_target")
    val pomodoroTarget: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
