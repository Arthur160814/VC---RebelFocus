package com.rebelfocus.core.model

/**
 * Domain model for tracking user progress and streaks.
 */
data class UserProgress(
    val id: String,
    val totalFocusTimeMillis: Long,
    val totalSessionsCompleted: Int,
    val currentStreakDays: Int,
    val longestStreakDays: Int,
    /** Wall-clock date of the last completed session. Null if none. */
    val lastSessionDate: Long?,
    val updatedAt: Long
)
