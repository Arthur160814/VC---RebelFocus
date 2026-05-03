package com.rebelfocus.core.data.repository

import com.rebelfocus.core.database.dao.ScheduledTriggerDao
import com.rebelfocus.core.database.entity.ScheduledTriggerEntity
import com.rebelfocus.core.model.ScheduledTrigger
import com.rebelfocus.core.model.ScheduledTriggerStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduledTriggerRepositoryImpl @Inject constructor(
    private val dao: ScheduledTriggerDao
) : ScheduledTriggerRepository {

    override suspend fun insertOrReplace(trigger: ScheduledTrigger) {
        dao.insertOrReplace(trigger.toEntity())
    }

    override suspend fun update(trigger: ScheduledTrigger) {
        dao.update(trigger.toEntity())
    }

    override suspend fun getById(id: String): ScheduledTrigger? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getByRuleIdAndStatus(ruleId: String, status: ScheduledTriggerStatus): List<ScheduledTrigger> {
        return dao.getByRuleIdAndStatus(ruleId, status).map { it.toDomain() }
    }

    override fun observeByStatus(status: ScheduledTriggerStatus): Flow<List<ScheduledTrigger>> {
        return dao.observeByStatus(status).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun updateStatuses(ids: List<String>, newStatus: ScheduledTriggerStatus) {
        dao.updateStatuses(ids, newStatus, System.currentTimeMillis())
    }
}

private fun ScheduledTriggerEntity.toDomain() = ScheduledTrigger(
    id = id,
    sourceRuleId = sourceRuleId,
    sourceEventId = sourceEventId,
    triggerAtMillis = triggerAtMillis,
    profileId = profileId,
    status = status,
    requestCode = requestCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun ScheduledTrigger.toEntity() = ScheduledTriggerEntity(
    id = id,
    sourceRuleId = sourceRuleId,
    sourceEventId = sourceEventId,
    triggerAtMillis = triggerAtMillis,
    profileId = profileId,
    status = status,
    requestCode = requestCode,
    createdAt = createdAt,
    updatedAt = updatedAt
)
