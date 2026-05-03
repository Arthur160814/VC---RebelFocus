package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rebelfocus.core.database.entity.AuditEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditEventDao {

    @Insert
    suspend fun insert(event: AuditEventEntity)

    @Insert
    suspend fun insertAll(events: List<AuditEventEntity>)

    @Query("SELECT * FROM audit_events ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<AuditEventEntity>>

    @Query("SELECT * FROM audit_events ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<AuditEventEntity>>

    @Query("SELECT * FROM audit_events WHERE session_id = :sessionId ORDER BY timestamp DESC")
    fun observeBySessionId(sessionId: String): Flow<List<AuditEventEntity>>

    @Query("SELECT * FROM audit_events WHERE event_type = :eventType ORDER BY timestamp DESC")
    fun observeByType(eventType: String): Flow<List<AuditEventEntity>>

    @Query("SELECT COUNT(*) FROM audit_events")
    suspend fun count(): Int

    @Query("DELETE FROM audit_events WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOlderThan(beforeTimestamp: Long): Int
}
