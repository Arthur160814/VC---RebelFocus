package com.rebelfocus.core.model

import java.time.LocalDate

enum class FocusMode {
    NORMAL, EXTREME, ULTIMATE
}

data class ModeUsageStats(
    val focusTimeMillis: Long,
    val completedSessions: Int,
    val completionRate: Float?, // null if no terminal sessions
    val totalTerminalSessions: Int
)

data class FocusStats(
    val totalSessions: Int,
    val totalFocusTimeMillis: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val dailyAverageMillis: Long,
    val completionRate: Float,
    val interruptedCount: Int,
    val bestDayName: String?,
    val bestDayFocusMillis: Long,
    val focusMinutesPerDay: Map<java.time.LocalDate, Long>,
    val selectedRange: StatsRange,
    val modeUsage: Map<FocusMode, ModeUsageStats>,
    val mostEffectiveMode: FocusMode?,
    val weeklyGoalTargetMinutes: Int,
    val weeklyGoalProgressMinutes: Int,
    val recentSessions: List<RecentSession>
)

data class RecentSession(
    val id: String,
    val dateLabel: String,
    val mode: FocusMode,
    val durationMillis: Long,
    val stateLabel: String
)
