package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.ScheduledTriggerEntity
import com.rebelfocus.core.model.ScheduledTriggerStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(trigger: ScheduledTriggerEntity)

    @Update
    suspend fun update(trigger: ScheduledTriggerEntity)

    @Query("SELECT * FROM scheduled_triggers WHERE id = :id")
    suspend fun getById(id: String): ScheduledTriggerEntity?

    @Query("SELECT * FROM scheduled_triggers WHERE sourceRuleId = :ruleId AND status = :status")
    suspend fun getByRuleIdAndStatus(ruleId: String, status: ScheduledTriggerStatus): List<ScheduledTriggerEntity>

    @Query("SELECT * FROM scheduled_triggers WHERE status = :status")
    fun observeByStatus(status: ScheduledTriggerStatus): Flow<List<ScheduledTriggerEntity>>

    @Query("UPDATE scheduled_triggers SET status = :newStatus, updatedAt = :updatedAt WHERE id IN (:ids)")
    suspend fun updateStatuses(ids: List<String>, newStatus: ScheduledTriggerStatus, updatedAt: Long)
}
