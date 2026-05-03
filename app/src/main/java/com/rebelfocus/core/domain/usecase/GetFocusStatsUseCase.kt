package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.database.entity.SessionEntity
import com.rebelfocus.core.model.FocusMode
import com.rebelfocus.core.model.FocusStats
import com.rebelfocus.core.model.ModeUsageStats
import com.rebelfocus.core.model.StatsRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetFocusStatsUseCase @Inject constructor(
    private val sessionDao: SessionDao
) {
    operator fun invoke(range: StatsRange): Flow<FocusStats> {
        return sessionDao.observeAll().map { sessions ->
            val today = LocalDate.now()
            val terminalStates = listOf("Completed", "Cancelled", "EmergencyExit", "Failed")
            val interruptedStates = listOf("Cancelled", "EmergencyExit", "Failed")
            
            // Filter sessions by range
            val sessionsInRange = when (range) {
                StatsRange.SEVEN_DAYS -> {
                    val cutoff = today.minusDays(6)
                    sessions.filter { getSessionEndDate(it) >= cutoff }
                }
                StatsRange.THIRTY_DAYS -> {
                    val cutoff = today.minusDays(29)
                    sessions.filter { getSessionEndDate(it) >= cutoff }
                }
                StatsRange.ALL -> sessions
            }

            val finishedSessions = sessionsInRange.filter { it.state in terminalStates }
            val completedSessions = finishedSessions.filter { it.state == "Completed" }
            val interruptedSessions = finishedSessions.filter { it.state in interruptedStates }
            
            val totalSessions = completedSessions.size
            val totalTime = completedSessions.sumOf { it.elapsedAtPauseMillis }

            // Mode Usage Analytics
            val modes = listOf(FocusMode.NORMAL, FocusMode.EXTREME, FocusMode.ULTIMATE)
            val modeUsage = modes.associateWith { mode ->
                val modeTerminalSessions = finishedSessions.filter { getFocusMode(it) == mode }
                val modeCompletedSessions = modeTerminalSessions.filter { it.state == "Completed" }
                
                val modeFocusTime = modeCompletedSessions.sumOf { it.elapsedAtPauseMillis }
                val modeFinishedCount = modeTerminalSessions.size
                
                ModeUsageStats(
                    focusTimeMillis = modeFocusTime,
                    completedSessions = modeCompletedSessions.size,
                    completionRate = if (modeFinishedCount > 0) {
                        modeCompletedSessions.size.toFloat() / modeFinishedCount.toFloat()
                    } else null,
                    totalTerminalSessions = modeFinishedCount
                )
            }

            val mostEffectiveMode = modeUsage.filter { it.value.totalTerminalSessions > 0 }
                .maxWithOrNull(
                    compareByDescending<Map.Entry<FocusMode, ModeUsageStats>> { it.value.completionRate ?: 0f }
                        .thenByDescending { it.value.focusTimeMillis }
                )?.key
            
            // Global streaks logic (still uses all sessions)
            val allCompletedSessions = sessions.filter { it.state == "Completed" }
            val completedDates = allCompletedSessions.map { getSessionEndDate(it) }.distinct().sortedDescending()
            val currentStreak = calculateCurrentStreak(completedDates)
            val longestStreak = calculateLongestStreak(completedDates)
            
            // Chart distribution
            val chartDays = if (range == StatsRange.SEVEN_DAYS) 7 else 30
            val chartRange = (0 until chartDays).map { today.minusDays(it.toLong()) }
            val focusMinutesPerDay = chartRange.associateWith { date ->
                allCompletedSessions.filter { getSessionEndDate(it) == date }.sumOf { it.elapsedAtPauseMillis }
            }

            // Daily Average
            val totalFocusInRange = focusMinutesPerDay.filter { it.key in chartRange }.values.sum()
            val dailyAverageMillis = when (range) {
                StatsRange.SEVEN_DAYS -> totalFocusInRange / 7
                StatsRange.THIRTY_DAYS -> totalFocusInRange / 30
                StatsRange.ALL -> {
                    val firstSessionDate = sessions.minByOrNull { it.createdAt }?.let { 
                        Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate() 
                    }
                    if (firstSessionDate != null) {
                        val days = java.time.temporal.ChronoUnit.DAYS.between(firstSessionDate, today) + 1
                        totalTime / days.coerceAtLeast(1)
                    } else 0L
                }
            }
            
            val finishedCount = finishedSessions.size
            val completionRate = if (finishedCount > 0) {
                completedSessions.size.toFloat() / finishedCount.toFloat()
            } else 0f
            
            val bestDayEntry = focusMinutesPerDay.maxByOrNull { it.value }
            val bestDayFocusMillis = bestDayEntry?.value ?: 0L
            val bestDayName = if (bestDayFocusMillis > 0L) {
                bestDayEntry?.key?.dayOfWeek?.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.getDefault())
            } else null

            FocusStats(
                totalSessions = totalSessions,
                totalFocusTimeMillis = totalTime,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                dailyAverageMillis = dailyAverageMillis,
                completionRate = completionRate,
                interruptedCount = interruptedSessions.size,
                bestDayName = bestDayName,
                bestDayFocusMillis = bestDayFocusMillis,
                focusMinutesPerDay = focusMinutesPerDay,
                selectedRange = range,
                modeUsage = modeUsage,
                mostEffectiveMode = mostEffectiveMode
            )
        }
    }

    private fun getSessionEndDate(session: SessionEntity): LocalDate {
        val millis = session.completedAt ?: session.updatedAt
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun getFocusMode(session: SessionEntity): FocusMode {
        return when {
            session.isUltimateMode -> FocusMode.ULTIMATE
            session.isExtremeMode -> FocusMode.EXTREME
            else -> FocusMode.NORMAL
        }
    }

    private fun calculateCurrentStreak(dates: List<LocalDate>): Int {
        if (dates.isEmpty()) return 0
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        
        if (dates[0] != today && dates[0] != yesterday) return 0
        
        var streak = 1
        for (i in 0 until dates.size - 1) {
            if (dates[i].minusDays(1) == dates[i+1]) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateLongestStreak(dates: List<LocalDate>): Int {
        if (dates.isEmpty()) return 0
        val sortedDates = dates.sorted() // Oldest to newest
        
        var maxStreak = 1
        var currentStreak = 1
        
        for (i in 0 until sortedDates.size - 1) {
            if (sortedDates[i].plusDays(1) == sortedDates[i+1]) {
                currentStreak++
            } else {
                maxStreak = maxOf(maxStreak, currentStreak)
                currentStreak = 1
            }
        }
        return maxOf(maxStreak, currentStreak)
    }
}
