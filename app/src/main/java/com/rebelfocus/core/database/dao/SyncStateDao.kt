package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.SyncStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncStateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(syncState: SyncStateEntity)

    @Update
    suspend fun update(syncState: SyncStateEntity)

    @Query("SELECT * FROM sync_state WHERE entity_type = :entityType")
    suspend fun getByEntityType(entityType: String): SyncStateEntity?

    @Query("SELECT * FROM sync_state ORDER BY updated_at DESC")
    fun observeAll(): Flow<List<SyncStateEntity>>

    @Query("DELETE FROM sync_state WHERE id = :id")
    suspend fun deleteById(id: String)
}
