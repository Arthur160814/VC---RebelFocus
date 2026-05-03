package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.database.entity.SessionEntity
import com.rebelfocus.core.model.FocusStats
import com.rebelfocus.core.model.StatsRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

import com.rebelfocus.core.data.datastore.UserPreferencesDataStore
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class GetFocusStatsUseCaseTest {

    private lateinit var fakeSessionDao: FakeSessionDao
    private lateinit var mockPrefs: UserPreferencesDataStore
    private lateinit var getFocusStatsUseCase: GetFocusStatsUseCase

    @Before
    fun setup() {
        fakeSessionDao = FakeSessionDao()
        mockPrefs = mockk()
        every { mockPrefs.weeklyFocusGoalMinutes } returns flowOf(120)
        getFocusStatsUseCase = GetFocusStatsUseCase(fakeSessionDao, mockPrefs)
    }

    @Test
    fun `calculate streaks correctly`() = runTest {
        val today = LocalDate.now()
        
        // Mock sessions: Today, Yesterday, Day before yesterday, and a gap, then 2 more days
        val sessions = listOf(
            createMockSession(today, "1"),
            createMockSession(today.minusDays(1), "2"),
            createMockSession(today.minusDays(2), "3"),
            createMockSession(today.minusDays(4), "4"),
            createMockSession(today.minusDays(5), "5")
        )

        fakeSessionDao.emit(sessions)

        val stats = getFocusStatsUseCase(StatsRange.ALL).first()

        assertEquals(3, stats.currentStreak)
        assertEquals(3, stats.longestStreak)
        assertEquals(5, stats.totalSessions)
        assertEquals(5, stats.recentSessions.size)
        assertEquals("Today", stats.recentSessions[0].dateLabel)
    }

    @Test
    fun `calculate current streak as 0 if gap is too large`() = runTest {
        val today = LocalDate.now()
        
        // Session 2 days ago (gap yesterday and today)
        val sessions = listOf(
            createMockSession(today.minusDays(2), "1")
        )

        fakeSessionDao.emit(sessions)

        val stats = getFocusStatsUseCase(StatsRange.ALL).first()

        assertEquals(0, stats.currentStreak)
        assertEquals(1, stats.longestStreak)
    }

    @Test
    fun `streak handles multiple sessions on same day`() = runTest {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        
        val sessions = listOf(
            createMockSession(today, "1"),
            createMockSession(today, "2"),
            createMockSession(yesterday, "3")
        )

        fakeSessionDao.emit(sessions)

        val stats = getFocusStatsUseCase(StatsRange.ALL).first()

        assertEquals(2, stats.currentStreak)
        assertEquals(2, stats.longestStreak)
    }

    private fun createMockSession(date: LocalDate, id: String): SessionEntity {
        val millis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return SessionEntity(
            id = id,
            profileId = "p1",
            sessionType = "Focus",
            state = "Completed",
            startTimeElapsedRealtime = 0L,
            plannedDurationMillis = 25 * 60 * 1000L,
            elapsedAtPauseMillis = 0L,
            breakDurationMillis = 0L,
            pomodoroCount = 1,
            pomodoroTarget = 1,
            createdAt = millis,
            updatedAt = millis + 1000,
            completedAt = millis + 1000
        )
    }

    private class FakeSessionDao : SessionDao {
        private val sessionsFlow = MutableStateFlow<List<SessionEntity>>(emptyList())

        suspend fun emit(sessions: List<SessionEntity>) {
            sessionsFlow.emit(sessions)
        }

        override fun observeCompletedSessions(): Flow<List<SessionEntity>> = sessionsFlow

        override suspend fun insert(session: SessionEntity) {}
        override suspend fun update(session: SessionEntity) {}
        override suspend fun getById(id: String): SessionEntity? = null
        override fun observeById(id: String): Flow<SessionEntity?> = MutableStateFlow(null)
        override fun observeAll(): Flow<List<SessionEntity>> = sessionsFlow
        override fun observeByStates(states: List<String>): Flow<List<SessionEntity>> = sessionsFlow
        override fun observeActiveSession(states: List<String>): Flow<SessionEntity?> = MutableStateFlow(null)
        override suspend fun getActiveSession(states: List<String>): SessionEntity? = null
        override suspend fun deleteById(id: String) {}
        override suspend fun count(): Int = sessionsFlow.value.size
        override suspend fun getCompletedSessionsSince(sinceMillis: Long): List<SessionEntity> = emptyList()
    }
}
