package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.backup.BackupContainerDto
import com.rebelfocus.core.database.dao.RestoreDao
import com.rebelfocus.core.database.entity.AutomationRuleEntity
import com.rebelfocus.core.database.entity.BlockedAppEntity
import com.rebelfocus.core.database.entity.FocusProfileEntity
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef
import com.rebelfocus.core.domain.backup.BackupProvider
import com.rebelfocus.core.model.CalendarMatchConfig
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ImportBackupUseCase @Inject constructor(
    private val restoreDao: RestoreDao,
    private val backupProvider: BackupProvider
) {
    suspend operator fun invoke(source: Any): Result<Int> {
        return runCatching {
            // 1. Fetch JSON from provider
            val json = backupProvider.importBackup(source).getOrThrow()

            // 2. Pre-transaction validation
            val container = try {
                Json.decodeFromString<BackupContainerDto>(json)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid backup format: ${e.message}")
            }

            // 3. Version validation
            if (container.version > BackupContainerDto.CURRENT_VERSION) {
                throw IllegalArgumentException("Backup version (${container.version}) is newer than app format version (${BackupContainerDto.CURRENT_VERSION}). Please update the app.")
            }

            // 4. Basic integrity check
            if (container.profiles.isEmpty() && container.automationRules.isEmpty()) {
                throw IllegalArgumentException("Backup contains no profiles or rules.")
            }

            // 5. Duplicate ID and Referential Integrity Validation
            val profileIds = container.profiles.map { it.id }.toSet()
            if (profileIds.size != container.profiles.size) {
                throw IllegalArgumentException("Backup contains duplicate Profile IDs.")
            }

            val appPackages = container.blockedApps.map { it.packageName }.toSet()
            if (appPackages.size != container.blockedApps.size) {
                throw IllegalArgumentException("Backup contains duplicate App Package Names.")
            }

            for (crossRef in container.profileBlockedAppCrossRefs) {
                if (crossRef.profileId !in profileIds) {
                    throw IllegalArgumentException("Cross-reference points to non-existent Profile: ${crossRef.profileId}")
                }
                if (crossRef.packageName !in appPackages) {
                    // Note: In v1 we assume all referenced apps must be in the backup
                    throw IllegalArgumentException("Cross-reference points to non-existent App: ${crossRef.packageName}")
                }
            }

            // 6. Map DTOs to Entities
            val profiles = container.profiles.map {
                FocusProfileEntity(
                    id = it.id,
                    name = it.name,
                    isDefault = it.isDefault,
                    sessionType = it.sessionType,
                    isExtremeMode = it.isExtremeMode,
                    // Enforce consistency: Ultimate requires Extreme
                    isUltimateMode = it.isUltimateMode && it.isExtremeMode,
                    focusDurationMillis = it.focusDurationMillis,
                    breakDurationMillis = it.breakDurationMillis,
                    pomodoroTarget = it.pomodoroTarget,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
            val apps = container.blockedApps.map {
                BlockedAppEntity(it.packageName, it.appName, it.isEnabled, it.createdAt, it.updatedAt)
            }
            val rules = container.automationRules.map {
                AutomationRuleEntity(
                    it.id, it.profileId, it.name, it.isEnabled, it.scheduleType, it.startTimeMinutes, it.endTimeMinutes, it.daysOfWeek,
                    it.calendarMatchConfig?.let { c ->
                        CalendarMatchConfig(c.calendarId, c.titleKeyword, c.requireBusyStatus, c.timeWindowStartMinutes, c.timeWindowEndMinutes)
                    },
                    it.createdAt, it.updatedAt
                )
            }
            val crossRefs = container.profileBlockedAppCrossRefs.map {
                ProfileBlockedAppCrossRef(it.profileId, it.packageName)
            }

            // 6. Transactional Apply
            restoreDao.restoreData(profiles, apps, rules, crossRefs)

            container.profiles.size
        }
    }
}
