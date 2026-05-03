package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for sync/backup metadata.
 * Tracks last sync state per entity type for conflict resolution.
 */
@Entity(tableName = "sync_state")
data class SyncStateEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "entity_type")
    val entityType: String,
    @ColumnInfo(name = "entity_id")
    val entityId: String?,
    @ColumnInfo(name = "last_sync_timestamp")
    val lastSyncTimestamp: Long?,
    @ColumnInfo(name = "last_sync_status")
    val lastSyncStatus: String?,
    @ColumnInfo(name = "sync_version")
    val syncVersion: Int,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
