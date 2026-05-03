package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.AuditEventRepository
import com.rebelfocus.core.database.dao.AuditEventDao
import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuditEventRepositoryImpl @Inject constructor(
    private val auditEventDao: AuditEventDao
) : AuditEventRepository {

    override suspend fun insert(event: AuditEvent) {
        auditEventDao.insert(event.toEntity())
    }

    override suspend fun insertAll(events: List<AuditEvent>) {
        auditEventDao.insertAll(events.map { it.toEntity() })
    }

    override fun observeAll(): Flow<List<AuditEvent>> {
        return auditEventDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeRecent(limit: Int): Flow<List<AuditEvent>> {
        return auditEventDao.observeRecent(limit).map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeBySessionId(sessionId: String): Flow<List<AuditEvent>> {
        return auditEventDao.observeBySessionId(sessionId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeByType(eventType: AuditEventType): Flow<List<AuditEvent>> {
        return auditEventDao.observeByType(eventType.name)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun count(): Int {
        return auditEventDao.count()
    }

    override suspend fun deleteOlderThan(beforeTimestamp: Long): Int {
        return auditEventDao.deleteOlderThan(beforeTimestamp)
    }
}
