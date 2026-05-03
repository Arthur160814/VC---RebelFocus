package com.rebelfocus.core.common.permissions

data class PermissionState(
    val accessibilityEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val exactAlarmsEnabled: Boolean = false,
    val batteryOptimizationDisabled: Boolean = false
) {
    val allGranted: Boolean
        get() = accessibilityEnabled && notificationsEnabled
}
