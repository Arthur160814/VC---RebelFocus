package com.rebelfocus.core.domain.calendar

data class DeviceCalendar(
    val id: String,
    val displayName: String,
    val accountName: String
)

data class CalendarEvent(
    val id: String,
    val title: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val isBusy: Boolean
)

interface CalendarSource {
    suspend fun getAvailableCalendars(): List<DeviceCalendar>
    suspend fun getEvents(calendarId: String, startTimeMillis: Long, endTimeMillis: Long): List<CalendarEvent>
}
