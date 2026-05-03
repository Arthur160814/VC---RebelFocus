package com.rebelfocus.services.calendar

import com.rebelfocus.core.domain.calendar.CalendarSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CalendarModule {

    @Binds
    abstract fun bindCalendarSource(
        deviceCalendarSource: DeviceCalendarSource
    ): CalendarSource
}
