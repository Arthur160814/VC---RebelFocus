package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for the audit trail.
 * Append-only — events are inserted but never updated.
 */
@Entity(tableName = "audit_events")
data class AuditEventEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "event_type")
    val eventType: String,
    @ColumnInfo(name = "session_id")
    val sessionId: String?,
    @ColumnInfo(name = "package_name")
    val packageName: String?,
    val details: String?,
    val timestamp: Long
)
