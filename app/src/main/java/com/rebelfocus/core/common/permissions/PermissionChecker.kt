package com.rebelfocus.core.common.permissions

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val ACCESSIBILITY_SERVICE_NAME =
            "com.rebelfocus/com.rebelfocus.services.accessibility.RebelFocusAccessibilityService"
    }

    fun check(): PermissionState = PermissionState(
        accessibilityEnabled = isAccessibilityEnabled(),
        notificationsEnabled = areNotificationsEnabled(),
        exactAlarmsEnabled = canScheduleExactAlarms(),
        batteryOptimizationDisabled = isBatteryOptimizationDisabled()
    )

    fun isBatteryOptimizationDisabled(): Boolean {
        val pm = context.getSystemService(android.os.PowerManager::class.java)
        return pm?.isIgnoringBatteryOptimizations(context.packageName) ?: true
    }

    fun isAccessibilityEnabled(): Boolean {
        val raw = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return TextUtils.SimpleStringSplitter(':').run {
            setString(raw)
            while (hasNext()) {
                if (next().equals(ACCESSIBILITY_SERVICE_NAME, ignoreCase = true)) return true
            }
            false
        }
    }

    fun areNotificationsEnabled(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun canScheduleExactAlarms(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
        val am = context.getSystemService(AlarmManager::class.java)
        return am.canScheduleExactAlarms()
    }
}
