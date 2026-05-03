package com.rebelfocus.core.model

import java.time.LocalDate

data class FocusStats(
    val totalSessions: Int,
    val totalFocusTimeMillis: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val sessionsPerDay: Map<LocalDate, Int> // Last 7 days
)
