package com.rebelfocus.core.model

enum class ScheduledTriggerStatus {
    PENDING,
    FIRED,
    CANCELLED,
    OBSOLETE,
    FAILED
}

data class ScheduledTrigger(
    val id: String,
    val sourceRuleId: String,
    val sourceEventId: String?, // Null for non-calendar rules if we eventually migrate them
    val triggerAtMillis: Long,
    val profileId: String,
    val status: ScheduledTriggerStatus,
    val requestCode: Int,
    val createdAt: Long,
    val updatedAt: Long
)
