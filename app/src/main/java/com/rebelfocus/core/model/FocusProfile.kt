package com.rebelfocus.core.model

/**
 * Domain model for a focus profile.
 *
 * A profile bundles a set of blocked apps with session configuration
 * so the user can quickly start a tailored focus session.
 */
data class FocusProfile(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val sessionType: SessionType,
    val isExtremeMode: Boolean = false,
    val isUltimateMode: Boolean = false,
    /** Default focus duration in milliseconds. */
    val focusDurationMillis: Long,
    /** Default break duration in milliseconds (Pomodoro). */
    val breakDurationMillis: Long,
    /** Default pomodoro target count. */
    val pomodoroTarget: Int,
    /** Apps blocked when this profile is active. */
    val blockedApps: List<BlockedApp>,
    val createdAt: Long,
    val updatedAt: Long
)
