package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Cross-reference table linking focus profiles to blocked apps (many-to-many).
 */
@Entity(
    tableName = "profile_blocked_app_cross_ref",
    primaryKeys = ["profile_id", "package_name"]
)
data class ProfileBlockedAppCrossRef(
    @ColumnInfo(name = "profile_id")
    val profileId: String,
    @ColumnInfo(name = "package_name")
    val packageName: String
)
