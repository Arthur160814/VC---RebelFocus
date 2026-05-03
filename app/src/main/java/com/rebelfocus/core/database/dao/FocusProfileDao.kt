package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rebelfocus.core.database.entity.BlockedAppEntity
import com.rebelfocus.core.database.entity.FocusProfileEntity
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: FocusProfileEntity)

    @Update
    suspend fun update(profile: FocusProfileEntity)

    @Query("SELECT * FROM focus_profiles WHERE id = :id")
    suspend fun getById(id: String): FocusProfileEntity?

    @Query("SELECT * FROM focus_profiles WHERE id = :id")
    fun observeById(id: String): Flow<FocusProfileEntity?>

    @Query("SELECT * FROM focus_profiles ORDER BY name ASC")
    fun observeAll(): Flow<List<FocusProfileEntity>>

    @Query("SELECT * FROM focus_profiles WHERE is_default = 1 LIMIT 1")
    suspend fun getDefault(): FocusProfileEntity?

    @Query("DELETE FROM focus_profiles WHERE id = :id")
    suspend fun deleteById(id: String)

    // Cross-reference operations for profile ↔ blocked app relationship

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRef(crossRef: ProfileBlockedAppCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossRefs(crossRefs: List<ProfileBlockedAppCrossRef>)

    @Query("DELETE FROM profile_blocked_app_cross_ref WHERE profile_id = :profileId")
    suspend fun deleteCrossRefsByProfileId(profileId: String)

    @Query("""
        SELECT ba.* FROM blocked_apps ba 
        INNER JOIN profile_blocked_app_cross_ref ref ON ba.package_name = ref.package_name 
        WHERE ref.profile_id = :profileId
    """)
    fun observeBlockedAppsForProfile(profileId: String): Flow<List<BlockedAppEntity>>

    @Query("""
        SELECT ba.* FROM blocked_apps ba 
        INNER JOIN profile_blocked_app_cross_ref ref ON ba.package_name = ref.package_name 
        WHERE ref.profile_id = :profileId
    """)
    suspend fun getBlockedAppsForProfile(profileId: String): List<BlockedAppEntity>

    /**
     * Replace all blocked app associations for a profile in a single transaction.
     */
    @Transaction
    suspend fun replaceBlockedAppsForProfile(profileId: String, packageNames: List<String>) {
        deleteCrossRefsByProfileId(profileId)
        val crossRefs = packageNames.map { ProfileBlockedAppCrossRef(profileId, it) }
        insertCrossRefs(crossRefs)
    }

    @Query("SELECT COUNT(*) FROM focus_profiles")
    suspend fun count(): Int

    @Query("SELECT * FROM profile_blocked_app_cross_ref")
    suspend fun getAllCrossRefs(): List<ProfileBlockedAppCrossRef>
}
