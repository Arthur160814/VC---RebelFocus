package com.rebelfocus.core.model

/**
 * Types of automation schedule rules.
 */
enum class ScheduleType {
    /** Fires once at a specific date/time. */
    OneTime,
    /** Fires on selected weekdays at a fixed time. */
    RecurringWeekday,
    /** Fires based on calendar event matching. */
    CalendarEvent
}
