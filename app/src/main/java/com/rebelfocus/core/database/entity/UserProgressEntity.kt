package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for tracking user progress and streaks.
 */
@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "total_focus_time_millis")
    val totalFocusTimeMillis: Long,
    @ColumnInfo(name = "total_sessions_completed")
    val totalSessionsCompleted: Int,
    @ColumnInfo(name = "current_streak_days")
    val currentStreakDays: Int,
    @ColumnInfo(name = "longest_streak_days")
    val longestStreakDays: Int,
    @ColumnInfo(name = "last_session_date")
    val lastSessionDate: Long?,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
