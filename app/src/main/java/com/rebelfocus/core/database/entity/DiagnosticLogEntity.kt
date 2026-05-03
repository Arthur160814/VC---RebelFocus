package com.rebelfocus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diagnostic_logs")
data class DiagnosticLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val level: String, // INFO, WARN, ERROR
    val component: String, // SESSION, SCHEDULING, SYNC, SYSTEM
    val message: String,
    val extraData: String? = null
)
