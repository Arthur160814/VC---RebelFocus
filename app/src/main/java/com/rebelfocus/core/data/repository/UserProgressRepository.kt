package com.rebelfocus.core.data.repository

import com.rebelfocus.core.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface UserProgressRepository {
    suspend fun insert(progress: UserProgress)
    suspend fun update(progress: UserProgress)
    suspend fun get(): UserProgress?
    fun observe(): Flow<UserProgress?>
    suspend fun deleteAll()
}
