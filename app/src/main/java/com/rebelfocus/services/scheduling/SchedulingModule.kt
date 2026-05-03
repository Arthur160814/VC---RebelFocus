package com.rebelfocus.services.scheduling

import com.rebelfocus.core.domain.scheduling.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulingModule {

    @Binds
    abstract fun bindAlarmScheduler(
        systemAlarmScheduler: SystemAlarmScheduler
    ): AlarmScheduler
}
