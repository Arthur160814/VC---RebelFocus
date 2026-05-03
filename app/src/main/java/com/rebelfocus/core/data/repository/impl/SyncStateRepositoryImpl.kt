package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.SyncStateRepository
import com.rebelfocus.core.database.dao.SyncStateDao
import com.rebelfocus.core.model.SyncState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncStateRepositoryImpl @Inject constructor(
    private val syncStateDao: SyncStateDao
) : SyncStateRepository {

    override suspend fun insert(syncState: SyncState) {
        syncStateDao.insert(syncState.toEntity())
    }

    override suspend fun update(syncState: SyncState) {
        syncStateDao.update(syncState.toEntity())
    }

    override suspend fun getByEntityType(entityType: String): SyncState? {
        return syncStateDao.getByEntityType(entityType)?.toDomain()
    }

    override fun observeAll(): Flow<List<SyncState>> {
        return syncStateDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun deleteById(id: String) {
        syncStateDao.deleteById(id)
    }
}
