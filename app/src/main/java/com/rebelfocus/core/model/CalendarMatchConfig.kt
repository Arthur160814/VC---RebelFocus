package com.rebelfocus.core.model

data class CalendarMatchConfig(
    val calendarId: String,
    val titleKeyword: String?,
    val requireBusyStatus: Boolean,
    val timeWindowStartMinutes: Int?,
    val timeWindowEndMinutes: Int?
)
