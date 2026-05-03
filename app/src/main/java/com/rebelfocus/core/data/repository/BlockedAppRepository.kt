package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.BlockedApp
import kotlinx.coroutines.flow.Flow

interface BlockedAppRepository {
    suspend fun insert(app: BlockedApp)
    suspend fun insertAll(apps: List<BlockedApp>)
    suspend fun update(app: BlockedApp)
    suspend fun getByPackageName(packageName: String): BlockedApp?
    fun observeAll(): Flow<List<BlockedApp>>
    fun observeEnabled(): Flow<List<BlockedApp>>
    suspend fun getEnabledPackageNames(): List<String>
    suspend fun deleteByPackageName(packageName: String)
    suspend fun count(): Int
}
