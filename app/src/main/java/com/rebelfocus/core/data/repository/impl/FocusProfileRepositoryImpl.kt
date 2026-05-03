package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.database.dao.FocusProfileDao
import com.rebelfocus.core.model.BlockedApp
import com.rebelfocus.core.model.FocusProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FocusProfileRepositoryImpl @Inject constructor(
    private val focusProfileDao: FocusProfileDao
) : FocusProfileRepository {

    override suspend fun insert(profile: FocusProfile) {
        focusProfileDao.insert(profile.toEntity())
        focusProfileDao.replaceBlockedAppsForProfile(
            profile.id,
            profile.blockedApps.map { it.packageName }
        )
    }

    override suspend fun update(profile: FocusProfile) {
        focusProfileDao.update(profile.toEntity())
        focusProfileDao.replaceBlockedAppsForProfile(
            profile.id,
            profile.blockedApps.map { it.packageName }
        )
    }

    override suspend fun getById(id: String): FocusProfile? {
        val entity = focusProfileDao.getById(id) ?: return null
        val apps = focusProfileDao.getBlockedAppsForProfile(id).map { it.toDomain() }
        return entity.toDomain(apps)
    }

    override fun observeById(id: String): Flow<FocusProfile?> {
        return focusProfileDao.observeById(id).map { entity ->
            if (entity == null) return@map null
            val apps = focusProfileDao.getBlockedAppsForProfile(id).map { it.toDomain() }
            entity.toDomain(apps)
        }
    }

    override fun observeAll(): Flow<List<FocusProfile>> {
        return focusProfileDao.observeAll().map { entities ->
            entities.map { entity ->
                val apps = focusProfileDao.getBlockedAppsForProfile(entity.id).map { it.toDomain() }
                entity.toDomain(apps)
            }
        }
    }

    override suspend fun getDefault(): FocusProfile? {
        val entity = focusProfileDao.getDefault() ?: return null
        val apps = focusProfileDao.getBlockedAppsForProfile(entity.id).map { it.toDomain() }
        return entity.toDomain(apps)
    }

    override suspend fun deleteById(id: String) {
        focusProfileDao.deleteCrossRefsByProfileId(id)
        focusProfileDao.deleteById(id)
    }

    override suspend fun replaceBlockedAppsForProfile(profileId: String, packageNames: List<String>) {
        focusProfileDao.replaceBlockedAppsForProfile(profileId, packageNames)
    }

    override fun observeBlockedAppsForProfile(profileId: String): Flow<List<BlockedApp>> {
        return focusProfileDao.observeBlockedAppsForProfile(profileId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun count(): Int {
        return focusProfileDao.count()
    }
}
