package com.rebelfocus.core.model

/**
 * Domain model for sync/backup metadata.
 */
data class SyncState(
    val id: String,
    /** The type of entity this sync state tracks (e.g., "session", "profile"). */
    val entityType: String,
    /** Optional specific entity ID this state refers to. */
    val entityId: String?,
    /** Wall-clock timestamp of the last successful sync. */
    val lastSyncTimestamp: Long?,
    /** Status of the last sync attempt (e.g., "success", "failure"). */
    val lastSyncStatus: String?,
    /** Monotonically increasing version for conflict resolution. */
    val syncVersion: Int,
    val updatedAt: Long
)
