package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.model.FocusStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GetFocusStatsUseCase @Inject constructor(
    private val sessionDao: SessionDao
) {
    operator fun invoke(): Flow<FocusStats> {
        return sessionDao.observeCompletedSessions().map { sessions ->
            val totalSessions = sessions.size
            val totalTime = sessions.sumOf { it.elapsedAtPauseMillis }
            
            val dates = sessions.map { 
                Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate() 
            }.distinct().sortedDescending()

            val currentStreak = calculateCurrentStreak(dates)
            val longestStreak = calculateLongestStreak(dates)
            
            // Last 7 days distribution
            val last7Days = (0..6).map { LocalDate.now().minusDays(it.toLong()) }
            val sessionsPerDay = last7Days.associateWith { date ->
                sessions.count { 
                    Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate() == date 
                }
            }

            FocusStats(
                totalSessions = totalSessions,
                totalFocusTimeMillis = totalTime,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                sessionsPerDay = sessionsPerDay
            )
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
