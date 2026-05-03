package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: UserProgressEntity)

    @Update
    suspend fun update(progress: UserProgressEntity)

    @Query("SELECT * FROM user_progress LIMIT 1")
    suspend fun get(): UserProgressEntity?

    @Query("SELECT * FROM user_progress LIMIT 1")
    fun observe(): Flow<UserProgressEntity?>

    @Query("DELETE FROM user_progress")
    suspend fun deleteAll()
}
