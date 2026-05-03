package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.BlockedApp
import com.rebelfocus.core.model.FocusProfile
import kotlinx.coroutines.flow.Flow

interface FocusProfileRepository {
    suspend fun insert(profile: FocusProfile)
    suspend fun update(profile: FocusProfile)
    suspend fun getById(id: String): FocusProfile?
    fun observeById(id: String): Flow<FocusProfile?>
    fun observeAll(): Flow<List<FocusProfile>>
    suspend fun getDefault(): FocusProfile?
    suspend fun deleteById(id: String)
    suspend fun replaceBlockedAppsForProfile(profileId: String, packageNames: List<String>)
    fun observeBlockedAppsForProfile(profileId: String): Flow<List<BlockedApp>>
    suspend fun count(): Int
}
