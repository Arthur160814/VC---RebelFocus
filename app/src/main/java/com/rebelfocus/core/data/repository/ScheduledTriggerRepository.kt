package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.ScheduledTrigger
import com.rebelfocus.core.model.ScheduledTriggerStatus
import kotlinx.coroutines.flow.Flow

interface ScheduledTriggerRepository {
    suspend fun insertOrReplace(trigger: ScheduledTrigger)
    suspend fun update(trigger: ScheduledTrigger)
    suspend fun getById(id: String): ScheduledTrigger?
    suspend fun getByRuleIdAndStatus(ruleId: String, status: ScheduledTriggerStatus): List<ScheduledTrigger>
    fun observeByStatus(status: ScheduledTriggerStatus): Flow<List<ScheduledTrigger>>
    suspend fun updateStatuses(ids: List<String>, newStatus: ScheduledTriggerStatus)
}
