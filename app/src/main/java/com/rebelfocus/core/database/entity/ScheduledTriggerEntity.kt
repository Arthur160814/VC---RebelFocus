package com.rebelfocus.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rebelfocus.core.model.ScheduledTriggerStatus

@Entity(
    tableName = "scheduled_triggers",
    indices = [
        androidx.room.Index("sourceRuleId"),
        androidx.room.Index("status")
    ]
)
data class ScheduledTriggerEntity(
    @PrimaryKey val id: String,
    val sourceRuleId: String,
    val sourceEventId: String?,
    val triggerAtMillis: Long,
    val profileId: String,
    val status: ScheduledTriggerStatus,
    val requestCode: Int,
    val createdAt: Long,
    val updatedAt: Long
)
