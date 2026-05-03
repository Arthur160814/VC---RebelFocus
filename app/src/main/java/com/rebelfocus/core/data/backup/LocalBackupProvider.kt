package com.rebelfocus.core.data.backup

import android.content.Context
import android.net.Uri
import com.rebelfocus.core.domain.backup.BackupProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of BackupProvider using Android Storage Access Framework (SAF).
 * Expects Uri as destination/source.
 */
class LocalBackupProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : BackupProvider {

    override suspend fun exportBackup(json: String, destination: Any?): Result<Unit> = withContext(Dispatchers.IO) {
        val uri = destination as? Uri ?: return@withContext Result.failure(IllegalArgumentException("Uri required"))
        
        runCatching {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                output.write(json.toByteArray())
            } ?: error("Could not open output stream for Uri: $uri")
        }
    }

    override suspend fun importBackup(source: Any?): Result<String> = withContext(Dispatchers.IO) {
        val uri = source as? Uri ?: return@withContext Result.failure(IllegalArgumentException("Uri required"))
        
        runCatching {
            context.contentResolver.openInputStream(uri)?.use { input ->
                input.bufferedReader().readText()
            } ?: error("Could not open input stream for Uri: $uri")
        }
    }
}
