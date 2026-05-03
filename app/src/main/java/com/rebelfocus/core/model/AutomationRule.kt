package com.rebelfocus.core.model

/**
 * Domain model for an automation rule that can trigger focus sessions.
 */
data class AutomationRule(
    val id: String,
    val profileId: String,
    val name: String,
    val isEnabled: Boolean,
    val scheduleType: ScheduleType,
    /** Minutes from midnight for the start time. Null for calendar-based rules. */
    val startTimeMinutes: Int?,
    /** Minutes from midnight for the end time. Null for calendar-based rules. */
    val endTimeMinutes: Int?,
    /** Days of week (1=Monday .. 7=Sunday). Empty for non-recurring rules. */
    val daysOfWeek: List<Int>,
    /** Configuration for calendar-based matching. Null for non-calendar rules. */
    val calendarMatchConfig: CalendarMatchConfig?,
    val createdAt: Long,
    val updatedAt: Long
)
