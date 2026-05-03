package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.SyncState
import kotlinx.coroutines.flow.Flow

interface SyncStateRepository {
    suspend fun insert(syncState: SyncState)
    suspend fun update(syncState: SyncState)
    suspend fun getByEntityType(entityType: String): SyncState?
    fun observeAll(): Flow<List<SyncState>>
    suspend fun deleteById(id: String)
}
