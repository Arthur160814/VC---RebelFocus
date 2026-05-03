package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.BlockedAppRepository
import com.rebelfocus.core.database.dao.BlockedAppDao
import com.rebelfocus.core.model.BlockedApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BlockedAppRepositoryImpl @Inject constructor(
    private val blockedAppDao: BlockedAppDao
) : BlockedAppRepository {

    override suspend fun insert(app: BlockedApp) {
        blockedAppDao.insert(app.toEntity())
    }

    override suspend fun insertAll(apps: List<BlockedApp>) {
        blockedAppDao.insertAll(apps.map { it.toEntity() })
    }

    override suspend fun update(app: BlockedApp) {
        blockedAppDao.update(app.toEntity())
    }

    override suspend fun getByPackageName(packageName: String): BlockedApp? {
        return blockedAppDao.getByPackageName(packageName)?.toDomain()
    }

    override fun observeAll(): Flow<List<BlockedApp>> {
        return blockedAppDao.observeAll().map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeEnabled(): Flow<List<BlockedApp>> {
        return blockedAppDao.observeEnabled().map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getEnabledPackageNames(): List<String> {
        return blockedAppDao.getEnabledPackageNames()
    }

    override suspend fun deleteByPackageName(packageName: String) {
        blockedAppDao.deleteByPackageName(packageName)
    }

    override suspend fun count(): Int {
        return blockedAppDao.count()
    }
}
