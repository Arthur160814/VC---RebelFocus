package com.rebelfocus.core.data.backup

import kotlinx.serialization.Serializable

@Serializable
data class BackupContainerDto(
    val version: Int,
    val exportTimestamp: Long,
    val profiles: List<FocusProfileDto>,
    val blockedApps: List<BlockedAppDto>,
    val automationRules: List<AutomationRuleDto>,
    val profileBlockedAppCrossRefs: List<ProfileBlockedAppCrossRefDto>
) {
    companion object {
        const val CURRENT_VERSION = 2
    }
}

@Serializable
data class FocusProfileDto(
    val id: String,
    val name: String,
    val isDefault: Boolean,
    val sessionType: String,
    val isExtremeMode: Boolean = false,
    val focusDurationMillis: Long,
    val breakDurationMillis: Long,
    val pomodoroTarget: Int,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class BlockedAppDto(
    val packageName: String,
    val appName: String,
    val isEnabled: Boolean,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

@Serializable
data class AutomationRuleDto(
    val id: String,
    val profileId: String,
    val name: String,
    val isEnabled: Boolean,
    val scheduleType: String,
    val startTimeMinutes: Int?,
    val endTimeMinutes: Int?,
    val daysOfWeek: String?,
    val calendarMatchConfig: CalendarMatchConfigDto?,
    val createdAt: Long,
    val updatedAt: Long
)

@Serializable
data class CalendarMatchConfigDto(
    val calendarId: String,
    val titleKeyword: String?,
    val requireBusyStatus: Boolean,
    val timeWindowStartMinutes: Int?,
    val timeWindowEndMinutes: Int?
)

@Serializable
data class ProfileBlockedAppCrossRefDto(
    val profileId: String,
    val packageName: String
)
