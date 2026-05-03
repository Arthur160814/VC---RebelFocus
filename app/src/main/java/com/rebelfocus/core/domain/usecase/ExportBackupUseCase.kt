package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.backup.AutomationRuleDto
import com.rebelfocus.core.data.backup.BackupContainerDto
import com.rebelfocus.core.data.backup.BlockedAppDto
import com.rebelfocus.core.data.backup.CalendarMatchConfigDto
import com.rebelfocus.core.data.backup.FocusProfileDto
import com.rebelfocus.core.data.backup.ProfileBlockedAppCrossRefDto
import com.rebelfocus.core.database.RebelFocusDatabase
import com.rebelfocus.core.database.dao.AutomationRuleDao
import com.rebelfocus.core.database.dao.BlockedAppDao
import com.rebelfocus.core.database.dao.FocusProfileDao
import com.rebelfocus.core.domain.backup.BackupProvider
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ExportBackupUseCase @Inject constructor(
    private val profileDao: FocusProfileDao,
    private val appDao: BlockedAppDao,
    private val ruleDao: AutomationRuleDao,
    private val database: RebelFocusDatabase, // To get cross-refs
    private val backupProvider: BackupProvider
) {
    suspend operator fun invoke(destination: Any): Result<Unit> {
        return runCatching {
            val profiles = profileDao.observeAll().first()
            val apps = appDao.observeAll().first()
            val rules = ruleDao.observeAll().first()
            
            // Fetch all cross-refs
            // Note: Since we don't have a specific observeAll for CrossRefs, we'll use a direct query
            val crossRefs = profileDao.getAllCrossRefs()

            val container = BackupContainerDto(
                version = BackupContainerDto.CURRENT_VERSION,
                exportTimestamp = System.currentTimeMillis(),
                profiles = profiles.map { 
                    FocusProfileDto(it.id, it.name, it.isDefault, it.sessionType, it.isExtremeMode, it.isUltimateMode, it.focusDurationMillis, it.breakDurationMillis, it.pomodoroTarget, it.createdAt, it.updatedAt)
                },
                blockedApps = apps.map {
                    BlockedAppDto(it.packageName, it.appName, it.isEnabled, it.createdAt, it.updatedAt)
                },
                automationRules = rules.map {
                    AutomationRuleDto(
                        it.id, it.profileId, it.name, it.isEnabled, it.scheduleType, it.startTimeMinutes, it.endTimeMinutes, it.daysOfWeek,
                        it.calendarMatchConfig?.let { c ->
                            CalendarMatchConfigDto(c.calendarId, c.titleKeyword, c.requireBusyStatus, c.timeWindowStartMinutes, c.timeWindowEndMinutes)
                        },
                        it.createdAt, it.updatedAt
                    )
                },
                profileBlockedAppCrossRefs = crossRefs.map {
                    ProfileBlockedAppCrossRefDto(it.profileId, it.packageName)
                }
            )

            val json = Json.encodeToString(container)
            backupProvider.exportBackup(json, destination).getOrThrow()
        }
    }
}
