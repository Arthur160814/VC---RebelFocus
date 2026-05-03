package com.rebelfocus.core.data.repository.impl

import com.rebelfocus.core.data.mapper.toDomain
import com.rebelfocus.core.data.mapper.toEntity
import com.rebelfocus.core.data.repository.UserProgressRepository
import com.rebelfocus.core.database.dao.UserProgressDao
import com.rebelfocus.core.model.UserProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProgressRepositoryImpl @Inject constructor(
    private val userProgressDao: UserProgressDao
) : UserProgressRepository {

    override suspend fun insert(progress: UserProgress) {
        userProgressDao.insert(progress.toEntity())
    }

    override suspend fun update(progress: UserProgress) {
        userProgressDao.update(progress.toEntity())
    }

    override suspend fun get(): UserProgress? {
        return userProgressDao.get()?.toDomain()
    }

    override fun observe(): Flow<UserProgress?> {
        return userProgressDao.observe().map { it?.toDomain() }
    }

    override suspend fun deleteAll() {
        userProgressDao.deleteAll()
    }
}
