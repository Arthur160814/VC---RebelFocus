package com.rebelfocus.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Extension property to create the DataStore instance. */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "rebel_focus_preferences"
)

/**
 * Wrapper around DataStore Preferences for lightweight app settings.
 *
 * Used for:
 * - onboarding completed flag
 * - emergency exit counter
 * - last-known permission state snapshots
 * - temporary settings
 * - lightweight preferences
 */
@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    // ── Keys ──────────────────────────────────────────

    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val EMERGENCY_EXIT_COUNT = intPreferencesKey("emergency_exit_count")
        val LAST_PERMISSION_CHECK = longPreferencesKey("last_permission_check")
        val ACCESSIBILITY_ENABLED = booleanPreferencesKey("accessibility_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val OVERLAY_ENABLED = booleanPreferencesKey("overlay_enabled")
        val LAST_ACTIVE_PROFILE_ID = stringPreferencesKey("last_active_profile_id")
    }

    // ── Onboarding ────────────────────────────────────

    val isOnboardingCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    // ── Emergency exit ────────────────────────────────

    val emergencyExitCount: Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.EMERGENCY_EXIT_COUNT] ?: 0
    }

    suspend fun incrementEmergencyExitCount() {
        dataStore.edit { prefs ->
            val current = prefs[Keys.EMERGENCY_EXIT_COUNT] ?: 0
            prefs[Keys.EMERGENCY_EXIT_COUNT] = current + 1
        }
    }

    suspend fun resetEmergencyExitCount() {
        dataStore.edit { prefs ->
            prefs[Keys.EMERGENCY_EXIT_COUNT] = 0
        }
    }

    // ── Permission state snapshots ────────────────────

    val accessibilityEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ACCESSIBILITY_ENABLED] ?: false
    }

    suspend fun setAccessibilityEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESSIBILITY_ENABLED] = enabled
        }
    }

    val notificationEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.NOTIFICATION_ENABLED] ?: false
    }

    suspend fun setNotificationEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATION_ENABLED] = enabled
        }
    }

    val overlayEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.OVERLAY_ENABLED] ?: false
    }

    suspend fun setOverlayEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[Keys.OVERLAY_ENABLED] = enabled
        }
    }

    // ── Last active profile ───────────────────────────

    val lastActiveProfileId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[Keys.LAST_ACTIVE_PROFILE_ID]
    }

    suspend fun setLastActiveProfileId(profileId: String?) {
        dataStore.edit { prefs ->
            if (profileId != null) {
                prefs[Keys.LAST_ACTIVE_PROFILE_ID] = profileId
            } else {
                prefs.remove(Keys.LAST_ACTIVE_PROFILE_ID)
            }
        }
    }
}
