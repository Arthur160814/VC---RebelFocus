package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.AutomationRule
import kotlinx.coroutines.flow.Flow

interface AutomationRuleRepository {
    suspend fun insert(rule: AutomationRule)
    suspend fun update(rule: AutomationRule)
    suspend fun getById(id: String): AutomationRule?
    fun observeAll(): Flow<List<AutomationRule>>
    fun observeEnabled(): Flow<List<AutomationRule>>
    fun observeByProfileId(profileId: String): Flow<List<AutomationRule>>
    suspend fun deleteById(id: String)
    suspend fun count(): Int
}
