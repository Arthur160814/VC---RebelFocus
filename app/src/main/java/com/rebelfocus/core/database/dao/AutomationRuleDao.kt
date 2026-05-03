package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rebelfocus.core.database.entity.AutomationRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutomationRuleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: AutomationRuleEntity)

    @Update
    suspend fun update(rule: AutomationRuleEntity)

    @Query("SELECT * FROM automation_rules WHERE id = :id")
    suspend fun getById(id: String): AutomationRuleEntity?

    @Query("SELECT * FROM automation_rules ORDER BY name ASC")
    fun observeAll(): Flow<List<AutomationRuleEntity>>

    @Query("SELECT * FROM automation_rules WHERE is_enabled = 1 ORDER BY name ASC")
    fun observeEnabled(): Flow<List<AutomationRuleEntity>>

    @Query("SELECT * FROM automation_rules WHERE profile_id = :profileId")
    fun observeByProfileId(profileId: String): Flow<List<AutomationRuleEntity>>

    @Query("DELETE FROM automation_rules WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM automation_rules")
    suspend fun count(): Int
}
