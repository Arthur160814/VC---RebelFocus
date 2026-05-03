package com.rebelfocus.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for automation rules that trigger focus sessions.
 */
@Entity(tableName = "automation_rules")
data class AutomationRuleEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "profile_id")
    val profileId: String,
    val name: String,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean,
    @ColumnInfo(name = "schedule_type")
    val scheduleType: String,
    @ColumnInfo(name = "start_time_minutes")
    val startTimeMinutes: Int?,
    @ColumnInfo(name = "end_time_minutes")
    val endTimeMinutes: Int?,
    @ColumnInfo(name = "days_of_week")
    val daysOfWeek: String?,
    @androidx.room.Embedded(prefix = "calendar_")
    val calendarMatchConfig: com.rebelfocus.core.model.CalendarMatchConfig?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
