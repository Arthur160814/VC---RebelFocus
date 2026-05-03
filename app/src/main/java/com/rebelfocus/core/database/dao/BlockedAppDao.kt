package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.BlockedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(app: BlockedAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(apps: List<BlockedAppEntity>)

    @Update
    suspend fun update(app: BlockedAppEntity)

    @Query("SELECT * FROM blocked_apps WHERE package_name = :packageName")
    suspend fun getByPackageName(packageName: String): BlockedAppEntity?

    @Query("SELECT * FROM blocked_apps ORDER BY app_name ASC")
    fun observeAll(): Flow<List<BlockedAppEntity>>

    @Query("SELECT * FROM blocked_apps WHERE is_enabled = 1 ORDER BY app_name ASC")
    fun observeEnabled(): Flow<List<BlockedAppEntity>>

    @Query("SELECT package_name FROM blocked_apps WHERE is_enabled = 1")
    suspend fun getEnabledPackageNames(): List<String>

    @Query("DELETE FROM blocked_apps WHERE package_name = :packageName")
    suspend fun deleteByPackageName(packageName: String)

    @Query("SELECT COUNT(*) FROM blocked_apps")
    suspend fun count(): Int
}
