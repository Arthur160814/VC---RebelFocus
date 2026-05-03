package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rebelfocus.core.database.entity.AutomationRuleEntity
import com.rebelfocus.core.database.entity.BlockedAppEntity
import com.rebelfocus.core.database.entity.FocusProfileEntity
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef

/**
 * Coordinator DAO for transactional backup restoration.
 * Implements "Last Write Wins" at the entity level.
 */
@Dao
interface RestoreDao {

    @Transaction
    suspend fun restoreData(
        profiles: List<FocusProfileEntity>,
        apps: List<BlockedAppEntity>,
        rules: List<AutomationRuleEntity>,
        crossRefs: List<ProfileBlockedAppCrossRef>
    ) {
        // 1. Restore Blocked Apps (Last Write Wins)
        for (app in apps) {
            val existing = getBlockedApp(app.packageName)
            if (existing == null || app.updatedAt > existing.updatedAt) {
                insertBlockedApp(app)
            }
        }

        // 2. Restore Profiles (Last Write Wins)
        for (profile in profiles) {
            val existing = getProfileById(profile.id)
            if (existing == null || profile.updatedAt > existing.updatedAt) {
                insertProfile(profile)
                
                // If we inserted or updated a profile, we must also sync its cross-refs.
                // We'll do this in the next step to ensure all apps are present.
            }
        }

        // 3. Restore Automation Rules (Last Write Wins)
        for (rule in rules) {
            val existing = getRuleById(rule.id)
            if (existing == null || rule.updatedAt > existing.updatedAt) {
                insertRule(rule)
            }
        }

        // 4. Restore Cross-References
        // We only restore cross-refs for profiles that were included in the backup.
        // First, clear cross-refs for profiles we just processed.
        val processedProfileIds = profiles.map { it.id }
        deleteCrossRefsForProfiles(processedProfileIds)
        insertCrossRefs(crossRefs)
    }

    // Helper queries

    @Query("SELECT * FROM focus_profiles WHERE id = :id")
    suspend fun getProfileById(id: String): FocusProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: FocusProfileEntity)

    @Query("SELECT * FROM blocked_apps WHERE package_name = :packageName")
    suspend fun getBlockedApp(packageName: String): BlockedAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockedApp(app: BlockedAppEntity)

    @Update
    suspend fun updateBlockedApp(app: BlockedAppEntity)

    @Query("SELECT * FROM automation_rules WHERE id = :id")
    suspend fun getRuleById(id: String): AutomationRuleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRule(rule: AutomationRuleEntity)

    @Query("DELETE FROM profile_blocked_app_cross_ref WHERE profile_id IN (:profileIds)")
    suspend fun deleteCrossRefsForProfiles(profileIds: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<ProfileBlockedAppCrossRef>)
}
