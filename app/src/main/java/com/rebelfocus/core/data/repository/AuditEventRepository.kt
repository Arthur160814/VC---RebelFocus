package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import kotlinx.coroutines.flow.Flow

interface AuditEventRepository {
    suspend fun insert(event: AuditEvent)
    suspend fun insertAll(events: List<AuditEvent>)
    fun observeAll(): Flow<List<AuditEvent>>
    fun observeRecent(limit: Int): Flow<List<AuditEvent>>
    fun observeBySessionId(sessionId: String): Flow<List<AuditEvent>>
    fun observeByType(eventType: AuditEventType): Flow<List<AuditEvent>>
    suspend fun count(): Int
    suspend fun deleteOlderThan(beforeTimestamp: Long): Int
}
