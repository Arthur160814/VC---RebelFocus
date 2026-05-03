package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.database.dao.AutomationRuleDao
import com.rebelfocus.core.model.AutomationRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AutomationRuleRepositoryImpl @Inject constructor(
    private val automationRuleDao: AutomationRuleDao
) : AutomationRuleRepository {

    override suspend fun insert(rule: AutomationRule) {
        automationRuleDao.insert(rule.toEntity())
    }

    override suspend fun update(rule: AutomationRule) {
        automationRuleDao.update(rule.toEntity())
    }

    override suspend fun getById(id: String): AutomationRule? {
        return automationRuleDao.getById(id)?.toDomain()
    }

    override fun observeAll(): Flow<List<AutomationRule>> {
        return automationRuleDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeEnabled(): Flow<List<AutomationRule>> {
        return automationRuleDao.observeEnabled().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeByProfileId(profileId: String): Flow<List<AutomationRule>> {
        return automationRuleDao.observeByProfileId(profileId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun deleteById(id: String) {
        automationRuleDao.deleteById(id)
    }

    override suspend fun count(): Int {
        return automationRuleDao.count()
    }
}
