package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)

    @Update
    suspend fun update(session: SessionEntity)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getById(id: String): SessionEntity?

    @Query("SELECT * FROM sessions WHERE id = :id")
    fun observeById(id: String): Flow<SessionEntity?>

    @Query("SELECT * FROM sessions ORDER BY created_at DESC")
    fun observeAll(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE state IN (:states) ORDER BY updated_at DESC")
    fun observeByStates(states: List<String>): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE state IN (:states) ORDER BY updated_at DESC LIMIT 1")
    fun observeActiveSession(states: List<String>): Flow<SessionEntity?>

    @Query("SELECT * FROM sessions WHERE state IN (:states) ORDER BY updated_at DESC LIMIT 1")
    suspend fun getActiveSession(states: List<String>): SessionEntity?

    @Query("DELETE FROM sessions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM sessions")
    suspend fun count(): Int

    @Query("SELECT * FROM sessions WHERE state = 'Completed' ORDER BY created_at DESC")
    fun observeCompletedSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions WHERE state = 'Completed' AND created_at >= :sinceMillis ORDER BY created_at DESC")
    suspend fun getCompletedSessionsSince(sinceMillis: Long): List<SessionEntity>
}
