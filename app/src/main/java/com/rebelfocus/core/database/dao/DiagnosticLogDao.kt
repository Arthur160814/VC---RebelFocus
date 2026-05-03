package com.rebelfocus.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.rebelfocus.core.database.entity.DiagnosticLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticLogDao {

    @Insert
    suspend fun insert(log: DiagnosticLogEntity)

    @Query("SELECT * FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 200")
    fun observeLatestLogs(): Flow<List<DiagnosticLogEntity>>

    @Query("DELETE FROM diagnostic_logs WHERE id NOT IN (SELECT id FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 200)")
    suspend fun pruneOldLogs()

    @Transaction
    suspend fun logAndPrune(log: DiagnosticLogEntity) {
        insert(log)
        pruneOldLogs()
    }

    @Query("DELETE FROM diagnostic_logs")
    suspend fun clearAll()
}
