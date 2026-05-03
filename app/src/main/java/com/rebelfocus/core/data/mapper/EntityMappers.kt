package com.rebelfocus.core.data.mapper

import com.rebelfocus.core.database.entity.AuditEventEntity
import com.rebelfocus.core.database.entity.AutomationRuleEntity
import com.rebelfocus.core.database.entity.BlockedAppEntity
import com.rebelfocus.core.database.entity.FocusProfileEntity
import com.rebelfocus.core.database.entity.SessionEntity
import com.rebelfocus.core.database.entity.SyncStateEntity
import com.rebelfocus.core.database.entity.UserProgressEntity
import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import com.rebelfocus.core.model.AutomationRule
import com.rebelfocus.core.model.BlockedApp
import com.rebelfocus.core.model.FocusProfile
import com.rebelfocus.core.model.ScheduleType
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import com.rebelfocus.core.model.SessionType
import com.rebelfocus.core.model.SyncState
import com.rebelfocus.core.model.UserProgress

// ──────────────────────────────────────────────
// Session
// ──────────────────────────────────────────────

fun SessionEntity.toDomain(): Session = Session(
    id = id,
    profileId = profileId,
    type = SessionType.valueOf(sessionType),
    state = SessionState.valueOf(state),
    startTimeElapsedRealtime = startTimeElapsedRealtime,
    plannedDurationMillis = plannedDurationMillis,
    elapsedAtPauseMillis = elapsedAtPauseMillis,
    breakDurationMillis = breakDurationMillis,
    pomodoroCount = pomodoroCount,
    pomodoroTarget = pomodoroTarget,
    isExtremeMode = isExtremeMode,
    isUltimateMode = isUltimateMode,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)

fun Session.toEntity(): SessionEntity = SessionEntity(
    id = id,
    profileId = profileId,
    sessionType = type.name,
    state = state.name,
    startTimeElapsedRealtime = startTimeElapsedRealtime,
    plannedDurationMillis = plannedDurationMillis,
    elapsedAtPauseMillis = elapsedAtPauseMillis,
    breakDurationMillis = breakDurationMillis,
    pomodoroCount = pomodoroCount,
    pomodoroTarget = pomodoroTarget,
    isExtremeMode = isExtremeMode,
    isUltimateMode = isUltimateMode,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt
)

// ──────────────────────────────────────────────
// BlockedApp
// ──────────────────────────────────────────────

fun BlockedAppEntity.toDomain() = BlockedApp(
    packageName = packageName,
    appName = appName,
    isEnabled = isEnabled,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun BlockedApp.toEntity() = BlockedAppEntity(
    packageName = packageName,
    appName = appName,
    isEnabled = isEnabled,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// ──────────────────────────────────────────────
// FocusProfile
// ──────────────────────────────────────────────

/**
 * Maps entity to domain model. Blocked apps must be provided separately
 * because they come from the cross-reference join query.
 */
fun FocusProfileEntity.toDomain(blockedApps: List<BlockedApp>): FocusProfile = FocusProfile(
    id = id,
    name = name,
    isDefault = isDefault,
    sessionType = SessionType.valueOf(sessionType),
    isExtremeMode = isExtremeMode,
    isUltimateMode = isUltimateMode,
    focusDurationMillis = focusDurationMillis,
    breakDurationMillis = breakDurationMillis,
    pomodoroTarget = pomodoroTarget,
    blockedApps = blockedApps,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun FocusProfile.toEntity(): FocusProfileEntity = FocusProfileEntity(
    id = id,
    name = name,
    isDefault = isDefault,
    sessionType = sessionType.name,
    isExtremeMode = isExtremeMode,
    isUltimateMode = isUltimateMode,
    focusDurationMillis = focusDurationMillis,
    breakDurationMillis = breakDurationMillis,
    pomodoroTarget = pomodoroTarget,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// ──────────────────────────────────────────────
// AutomationRule
// ──────────────────────────────────────────────

fun AutomationRuleEntity.toDomain(): AutomationRule = AutomationRule(
    id = id,
    profileId = profileId,
    name = name,
    isEnabled = isEnabled,
    scheduleType = ScheduleType.valueOf(scheduleType),
    startTimeMinutes = startTimeMinutes,
    endTimeMinutes = endTimeMinutes,
    daysOfWeek = daysOfWeek?.split(",")?.mapNotNull { it.trim().toIntOrNull() } ?: emptyList(),
    calendarMatchConfig = calendarMatchConfig,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun AutomationRule.toEntity(): AutomationRuleEntity = AutomationRuleEntity(
    id = id,
    profileId = profileId,
    name = name,
    isEnabled = isEnabled,
    scheduleType = scheduleType.name,
    startTimeMinutes = startTimeMinutes,
    endTimeMinutes = endTimeMinutes,
    daysOfWeek = if (daysOfWeek.isEmpty()) null else daysOfWeek.joinToString(","),
    calendarMatchConfig = calendarMatchConfig,
    createdAt = createdAt,
    updatedAt = updatedAt
)

// ──────────────────────────────────────────────
// UserProgress
// ──────────────────────────────────────────────

fun UserProgressEntity.toDomain(): UserProgress = UserProgress(
    id = id,
    totalFocusTimeMillis = totalFocusTimeMillis,
    totalSessionsCompleted = totalSessionsCompleted,
    currentStreakDays = currentStreakDays,
    longestStreakDays = longestStreakDays,
    lastSessionDate = lastSessionDate,
    updatedAt = updatedAt
)

fun UserProgress.toEntity(): UserProgressEntity = UserProgressEntity(
    id = id,
    totalFocusTimeMillis = totalFocusTimeMillis,
    totalSessionsCompleted = totalSessionsCompleted,
    currentStreakDays = currentStreakDays,
    longestStreakDays = longestStreakDays,
    lastSessionDate = lastSessionDate,
    updatedAt = updatedAt
)

// ──────────────────────────────────────────────
// AuditEvent
// ──────────────────────────────────────────────

fun AuditEventEntity.toDomain(): AuditEvent = AuditEvent(
    id = id,
    eventType = AuditEventType.valueOf(eventType),
    sessionId = sessionId,
    packageName = packageName,
    details = details,
    timestamp = timestamp
)

fun AuditEvent.toEntity(): AuditEventEntity = AuditEventEntity(
    id = id,
    eventType = eventType.name,
    sessionId = sessionId,
    packageName = packageName,
    details = details,
    timestamp = timestamp
)

// ──────────────────────────────────────────────
// SyncState
// ──────────────────────────────────────────────

fun SyncStateEntity.toDomain(): SyncState = SyncState(
    id = id,
    entityType = entityType,
    entityId = entityId,
    lastSyncTimestamp = lastSyncTimestamp,
    lastSyncStatus = lastSyncStatus,
    syncVersion = syncVersion,
    updatedAt = updatedAt
)

fun SyncState.toEntity(): SyncStateEntity = SyncStateEntity(
    id = id,
    entityType = entityType,
    entityId = entityId,
    lastSyncTimestamp = lastSyncTimestamp,
    lastSyncStatus = lastSyncStatus,
    syncVersion = syncVersion,
    updatedAt = updatedAt
)
