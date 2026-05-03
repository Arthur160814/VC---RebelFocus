package com.rebelfocus.services.calendar

import android.content.Context
import android.provider.CalendarContract
import com.rebelfocus.core.domain.calendar.CalendarEvent
import com.rebelfocus.core.domain.calendar.CalendarSource
import com.rebelfocus.core.domain.calendar.DeviceCalendar
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceCalendarSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CalendarSource {

    override suspend fun getAvailableCalendars(): List<DeviceCalendar> = withContext(Dispatchers.IO) {
        val calendars = mutableListOf<DeviceCalendar>()
        
        // Requires READ_CALENDAR permission
        val uri = CalendarContract.Calendars.CONTENT_URI
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME
        )

        try {
            context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Calendars._ID)
                val nameIdx = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                val accountIdx = cursor.getColumnIndexOrThrow(CalendarContract.Calendars.ACCOUNT_NAME)

                while (cursor.moveToNext()) {
                    calendars.add(
                        DeviceCalendar(
                            id = cursor.getString(idIdx),
                            displayName = cursor.getString(nameIdx),
                            accountName = cursor.getString(accountIdx)
                        )
                    )
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        calendars
    }

    override suspend fun getEvents(calendarId: String, startTimeMillis: Long, endTimeMillis: Long): List<CalendarEvent> = withContext(Dispatchers.IO) {
        val events = mutableListOf<CalendarEvent>()

        val builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        android.content.ContentUris.appendId(builder, startTimeMillis)
        android.content.ContentUris.appendId(builder, endTimeMillis)

        val uri = builder.build()

        val projection = arrayOf(
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.AVAILABILITY
        )

        val selection = "${CalendarContract.Instances.CALENDAR_ID} = ?"
        val selectionArgs = arrayOf(calendarId)

        try {
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
                val idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.EVENT_ID)
                val titleIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.TITLE)
                val beginIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.BEGIN)
                val endIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.END)
                val availIdx = cursor.getColumnIndexOrThrow(CalendarContract.Instances.AVAILABILITY)

                while (cursor.moveToNext()) {
                    val availability = cursor.getInt(availIdx)
                    // CalendarContract.Events.AVAILABILITY_BUSY == 0
                    val isBusy = availability == CalendarContract.Events.AVAILABILITY_BUSY

                    events.add(
                        CalendarEvent(
                            id = cursor.getString(idIdx), // EVENT_ID. Combined with BEGIN in trigger identity to disambiguate recurring instances.
                            title = cursor.getString(titleIdx) ?: "",
                            startTimeMillis = cursor.getLong(beginIdx),
                            endTimeMillis = cursor.getLong(endIdx),
                            isBusy = isBusy
                        )
                    )
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        events
    }
}
