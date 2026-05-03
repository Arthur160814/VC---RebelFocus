package com.rebelfocus.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

@HiltAndroidApp
class RebelFocusApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
