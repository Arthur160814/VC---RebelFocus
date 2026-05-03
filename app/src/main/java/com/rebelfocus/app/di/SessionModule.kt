package com.rebelfocus.app.di

import com.rebelfocus.core.domain.session.SessionStateMachine
import com.rebelfocus.core.domain.session.SessionTimingCalculator
import com.rebelfocus.core.domain.session.SystemTimeSource
import com.rebelfocus.core.domain.session.TimeSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideTimeSource(): TimeSource = SystemTimeSource()

    @Provides
    @Singleton
    fun provideSessionStateMachine(): SessionStateMachine = SessionStateMachine()

    @Provides
    @Singleton
    fun provideSessionTimingCalculator(): SessionTimingCalculator = SessionTimingCalculator()
}
