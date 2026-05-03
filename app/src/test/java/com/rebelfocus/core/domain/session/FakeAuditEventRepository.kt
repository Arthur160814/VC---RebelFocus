package com.rebelfocus.core.domain.session

import com.rebelfocus.core.data.repository.AuditEventRepository
import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory [AuditEventRepository] for unit tests.
 */
class FakeAuditEventRepository : AuditEventRepository {

    private val events = mutableListOf<AuditEvent>()
    private val flow = MutableStateFlow<List<AuditEvent>>(emptyList())

    private fun emitAll() {
        flow.value = events.toList()
    }

    /** All recorded events, for test assertions. */
    fun allEvents(): List<AuditEvent> = events.toList()

    override suspend fun insert(event: AuditEvent) {
        events.add(event)
        emitAll()
    }

    override suspend fun insertAll(newEvents: List<AuditEvent>) {
        events.addAll(newEvents)
        emitAll()
    }

    override fun observeAll(): Flow<List<AuditEvent>> = flow

    override fun observeRecent(limit: Int): Flow<List<AuditEvent>> =
        flow.map { it.takeLast(limit) }

    override fun observeBySessionId(sessionId: String): Flow<List<AuditEvent>> =
        flow.map { list -> list.filter { it.sessionId == sessionId } }

    override fun observeByType(eventType: AuditEventType): Flow<List<AuditEvent>> =
        flow.map { list -> list.filter { it.eventType == eventType } }

    override suspend fun count(): Int = events.size

    override suspend fun deleteOlderThan(beforeTimestamp: Long): Int {
        val before = events.count { it.timestamp < beforeTimestamp }
        events.removeAll { it.timestamp < beforeTimestamp }
        emitAll()
        return before
    }
}
