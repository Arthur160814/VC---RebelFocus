package com.rebelfocus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.rebelfocus.app.navigation.AppNavigation
import com.rebelfocus.core.data.datastore.UserPreferencesDataStore
import com.rebelfocus.core.designsystem.RebelFocusTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesDataStore: UserPreferencesDataStore

    @Inject
    lateinit var focusSessionOrchestrator: com.rebelfocus.services.foreground.FocusSessionOrchestrator

    @Inject
    lateinit var notificationHelper: com.rebelfocus.services.foreground.NotificationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        focusSessionOrchestrator.startObserving()
        notificationHelper.createChannels()
        setContent {
            RebelFocusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(preferencesDataStore)
                }
            }
        }
    }
}

