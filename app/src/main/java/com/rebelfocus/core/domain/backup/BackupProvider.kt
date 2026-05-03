package com.rebelfocus.core.domain.backup

import android.net.Uri

/**
 * Abstraction for backup storage providers.
 */
interface BackupProvider {
    /**
     * Serializes and writes the backup data to the provider's storage.
     */
    suspend fun exportBackup(json: String, destination: Any?): Result<Unit>

    /**
     * Reads and returns the backup data from the provider's storage.
     */
    suspend fun importBackup(source: Any?): Result<String>
}
