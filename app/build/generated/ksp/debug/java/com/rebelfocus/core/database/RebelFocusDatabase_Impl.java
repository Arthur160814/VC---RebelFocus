package com.rebelfocus.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.rebelfocus.core.database.dao.AuditEventDao;
import com.rebelfocus.core.database.dao.AuditEventDao_Impl;
import com.rebelfocus.core.database.dao.AutomationRuleDao;
import com.rebelfocus.core.database.dao.AutomationRuleDao_Impl;
import com.rebelfocus.core.database.dao.BlockedAppDao;
import com.rebelfocus.core.database.dao.BlockedAppDao_Impl;
import com.rebelfocus.core.database.dao.DiagnosticLogDao;
import com.rebelfocus.core.database.dao.DiagnosticLogDao_Impl;
import com.rebelfocus.core.database.dao.FocusProfileDao;
import com.rebelfocus.core.database.dao.FocusProfileDao_Impl;
import com.rebelfocus.core.database.dao.RestoreDao;
import com.rebelfocus.core.database.dao.RestoreDao_Impl;
import com.rebelfocus.core.database.dao.ScheduledTriggerDao;
import com.rebelfocus.core.database.dao.ScheduledTriggerDao_Impl;
import com.rebelfocus.core.database.dao.SessionDao;
import com.rebelfocus.core.database.dao.SessionDao_Impl;
import com.rebelfocus.core.database.dao.SyncStateDao;
import com.rebelfocus.core.database.dao.SyncStateDao_Impl;
import com.rebelfocus.core.database.dao.UserProgressDao;
import com.rebelfocus.core.database.dao.UserProgressDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RebelFocusDatabase_Impl extends RebelFocusDatabase {
  private volatile SessionDao _sessionDao;

  private volatile BlockedAppDao _blockedAppDao;

  private volatile FocusProfileDao _focusProfileDao;

  private volatile AutomationRuleDao _automationRuleDao;

  private volatile UserProgressDao _userProgressDao;

  private volatile AuditEventDao _auditEventDao;

  private volatile SyncStateDao _syncStateDao;

  private volatile ScheduledTriggerDao _scheduledTriggerDao;

  private volatile RestoreDao _restoreDao;

  private volatile DiagnosticLogDao _diagnosticLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `focus_profiles` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `is_default` INTEGER NOT NULL, `session_type` TEXT NOT NULL, `is_extreme_mode` INTEGER NOT NULL, `is_ultimate_mode` INTEGER NOT NULL, `focus_duration_millis` INTEGER NOT NULL, `break_duration_millis` INTEGER NOT NULL, `pomodoro_target` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blocked_apps` (`package_name` TEXT NOT NULL, `app_name` TEXT NOT NULL, `is_enabled` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`package_name`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `profile_blocked_app_cross_ref` (`profile_id` TEXT NOT NULL, `package_name` TEXT NOT NULL, PRIMARY KEY(`profile_id`, `package_name`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sessions` (`id` TEXT NOT NULL, `profile_id` TEXT, `session_type` TEXT NOT NULL, `state` TEXT NOT NULL, `start_time_elapsed_realtime` INTEGER, `planned_duration_millis` INTEGER NOT NULL, `elapsed_at_pause_millis` INTEGER NOT NULL, `break_duration_millis` INTEGER NOT NULL, `pomodoro_count` INTEGER NOT NULL, `pomodoro_target` INTEGER NOT NULL, `is_extreme_mode` INTEGER NOT NULL, `is_ultimate_mode` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `completed_at` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `automation_rules` (`id` TEXT NOT NULL, `profile_id` TEXT NOT NULL, `name` TEXT NOT NULL, `is_enabled` INTEGER NOT NULL, `schedule_type` TEXT NOT NULL, `start_time_minutes` INTEGER, `end_time_minutes` INTEGER, `days_of_week` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `calendar_calendarId` TEXT, `calendar_titleKeyword` TEXT, `calendar_requireBusyStatus` INTEGER, `calendar_timeWindowStartMinutes` INTEGER, `calendar_timeWindowEndMinutes` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `audit_events` (`id` TEXT NOT NULL, `event_type` TEXT NOT NULL, `session_id` TEXT, `package_name` TEXT, `details` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sync_state` (`id` TEXT NOT NULL, `entity_type` TEXT NOT NULL, `entity_id` TEXT, `last_sync_timestamp` INTEGER, `last_sync_status` TEXT, `sync_version` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scheduled_triggers` (`id` TEXT NOT NULL, `sourceRuleId` TEXT NOT NULL, `sourceEventId` TEXT, `triggerAtMillis` INTEGER NOT NULL, `profileId` TEXT NOT NULL, `status` TEXT NOT NULL, `requestCode` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_scheduled_triggers_sourceRuleId` ON `scheduled_triggers` (`sourceRuleId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_scheduled_triggers_status` ON `scheduled_triggers` (`status`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `diagnostic_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `level` TEXT NOT NULL, `component` TEXT NOT NULL, `message` TEXT NOT NULL, `extraData` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_progress` (`id` TEXT NOT NULL, `total_focus_time_millis` INTEGER NOT NULL, `total_sessions_completed` INTEGER NOT NULL, `current_streak_days` INTEGER NOT NULL, `longest_streak_days` INTEGER NOT NULL, `last_session_date` INTEGER, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '69eb44fb0e448ba306086f1bfecefa00')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `focus_profiles`");
        db.execSQL("DROP TABLE IF EXISTS `blocked_apps`");
        db.execSQL("DROP TABLE IF EXISTS `profile_blocked_app_cross_ref`");
        db.execSQL("DROP TABLE IF EXISTS `sessions`");
        db.execSQL("DROP TABLE IF EXISTS `automation_rules`");
        db.execSQL("DROP TABLE IF EXISTS `audit_events`");
        db.execSQL("DROP TABLE IF EXISTS `sync_state`");
        db.execSQL("DROP TABLE IF EXISTS `scheduled_triggers`");
        db.execSQL("DROP TABLE IF EXISTS `diagnostic_logs`");
        db.execSQL("DROP TABLE IF EXISTS `user_progress`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsFocusProfiles = new HashMap<String, TableInfo.Column>(11);
        _columnsFocusProfiles.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("is_default", new TableInfo.Column("is_default", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("session_type", new TableInfo.Column("session_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("is_extreme_mode", new TableInfo.Column("is_extreme_mode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("is_ultimate_mode", new TableInfo.Column("is_ultimate_mode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("focus_duration_millis", new TableInfo.Column("focus_duration_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("break_duration_millis", new TableInfo.Column("break_duration_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("pomodoro_target", new TableInfo.Column("pomodoro_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFocusProfiles.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFocusProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFocusProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFocusProfiles = new TableInfo("focus_profiles", _columnsFocusProfiles, _foreignKeysFocusProfiles, _indicesFocusProfiles);
        final TableInfo _existingFocusProfiles = TableInfo.read(db, "focus_profiles");
        if (!_infoFocusProfiles.equals(_existingFocusProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "focus_profiles(com.rebelfocus.core.database.entity.FocusProfileEntity).\n"
                  + " Expected:\n" + _infoFocusProfiles + "\n"
                  + " Found:\n" + _existingFocusProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsBlockedApps = new HashMap<String, TableInfo.Column>(5);
        _columnsBlockedApps.put("package_name", new TableInfo.Column("package_name", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("app_name", new TableInfo.Column("app_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("is_enabled", new TableInfo.Column("is_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlockedApps.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlockedApps = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBlockedApps = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBlockedApps = new TableInfo("blocked_apps", _columnsBlockedApps, _foreignKeysBlockedApps, _indicesBlockedApps);
        final TableInfo _existingBlockedApps = TableInfo.read(db, "blocked_apps");
        if (!_infoBlockedApps.equals(_existingBlockedApps)) {
          return new RoomOpenHelper.ValidationResult(false, "blocked_apps(com.rebelfocus.core.database.entity.BlockedAppEntity).\n"
                  + " Expected:\n" + _infoBlockedApps + "\n"
                  + " Found:\n" + _existingBlockedApps);
        }
        final HashMap<String, TableInfo.Column> _columnsProfileBlockedAppCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsProfileBlockedAppCrossRef.put("profile_id", new TableInfo.Column("profile_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfileBlockedAppCrossRef.put("package_name", new TableInfo.Column("package_name", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfileBlockedAppCrossRef = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfileBlockedAppCrossRef = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfileBlockedAppCrossRef = new TableInfo("profile_blocked_app_cross_ref", _columnsProfileBlockedAppCrossRef, _foreignKeysProfileBlockedAppCrossRef, _indicesProfileBlockedAppCrossRef);
        final TableInfo _existingProfileBlockedAppCrossRef = TableInfo.read(db, "profile_blocked_app_cross_ref");
        if (!_infoProfileBlockedAppCrossRef.equals(_existingProfileBlockedAppCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "profile_blocked_app_cross_ref(com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef).\n"
                  + " Expected:\n" + _infoProfileBlockedAppCrossRef + "\n"
                  + " Found:\n" + _existingProfileBlockedAppCrossRef);
        }
        final HashMap<String, TableInfo.Column> _columnsSessions = new HashMap<String, TableInfo.Column>(15);
        _columnsSessions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("profile_id", new TableInfo.Column("profile_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("session_type", new TableInfo.Column("session_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("start_time_elapsed_realtime", new TableInfo.Column("start_time_elapsed_realtime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("planned_duration_millis", new TableInfo.Column("planned_duration_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("elapsed_at_pause_millis", new TableInfo.Column("elapsed_at_pause_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("break_duration_millis", new TableInfo.Column("break_duration_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("pomodoro_count", new TableInfo.Column("pomodoro_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("pomodoro_target", new TableInfo.Column("pomodoro_target", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("is_extreme_mode", new TableInfo.Column("is_extreme_mode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("is_ultimate_mode", new TableInfo.Column("is_ultimate_mode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSessions.put("completed_at", new TableInfo.Column("completed_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSessions = new TableInfo("sessions", _columnsSessions, _foreignKeysSessions, _indicesSessions);
        final TableInfo _existingSessions = TableInfo.read(db, "sessions");
        if (!_infoSessions.equals(_existingSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "sessions(com.rebelfocus.core.database.entity.SessionEntity).\n"
                  + " Expected:\n" + _infoSessions + "\n"
                  + " Found:\n" + _existingSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsAutomationRules = new HashMap<String, TableInfo.Column>(15);
        _columnsAutomationRules.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("profile_id", new TableInfo.Column("profile_id", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("is_enabled", new TableInfo.Column("is_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("schedule_type", new TableInfo.Column("schedule_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("start_time_minutes", new TableInfo.Column("start_time_minutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("end_time_minutes", new TableInfo.Column("end_time_minutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("days_of_week", new TableInfo.Column("days_of_week", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("calendar_calendarId", new TableInfo.Column("calendar_calendarId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("calendar_titleKeyword", new TableInfo.Column("calendar_titleKeyword", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("calendar_requireBusyStatus", new TableInfo.Column("calendar_requireBusyStatus", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("calendar_timeWindowStartMinutes", new TableInfo.Column("calendar_timeWindowStartMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAutomationRules.put("calendar_timeWindowEndMinutes", new TableInfo.Column("calendar_timeWindowEndMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAutomationRules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAutomationRules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAutomationRules = new TableInfo("automation_rules", _columnsAutomationRules, _foreignKeysAutomationRules, _indicesAutomationRules);
        final TableInfo _existingAutomationRules = TableInfo.read(db, "automation_rules");
        if (!_infoAutomationRules.equals(_existingAutomationRules)) {
          return new RoomOpenHelper.ValidationResult(false, "automation_rules(com.rebelfocus.core.database.entity.AutomationRuleEntity).\n"
                  + " Expected:\n" + _infoAutomationRules + "\n"
                  + " Found:\n" + _existingAutomationRules);
        }
        final HashMap<String, TableInfo.Column> _columnsAuditEvents = new HashMap<String, TableInfo.Column>(6);
        _columnsAuditEvents.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditEvents.put("event_type", new TableInfo.Column("event_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditEvents.put("session_id", new TableInfo.Column("session_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditEvents.put("package_name", new TableInfo.Column("package_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditEvents.put("details", new TableInfo.Column("details", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAuditEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAuditEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAuditEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAuditEvents = new TableInfo("audit_events", _columnsAuditEvents, _foreignKeysAuditEvents, _indicesAuditEvents);
        final TableInfo _existingAuditEvents = TableInfo.read(db, "audit_events");
        if (!_infoAuditEvents.equals(_existingAuditEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "audit_events(com.rebelfocus.core.database.entity.AuditEventEntity).\n"
                  + " Expected:\n" + _infoAuditEvents + "\n"
                  + " Found:\n" + _existingAuditEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsSyncState = new HashMap<String, TableInfo.Column>(7);
        _columnsSyncState.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("entity_type", new TableInfo.Column("entity_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("entity_id", new TableInfo.Column("entity_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("last_sync_timestamp", new TableInfo.Column("last_sync_timestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("last_sync_status", new TableInfo.Column("last_sync_status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("sync_version", new TableInfo.Column("sync_version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSyncState.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSyncState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSyncState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSyncState = new TableInfo("sync_state", _columnsSyncState, _foreignKeysSyncState, _indicesSyncState);
        final TableInfo _existingSyncState = TableInfo.read(db, "sync_state");
        if (!_infoSyncState.equals(_existingSyncState)) {
          return new RoomOpenHelper.ValidationResult(false, "sync_state(com.rebelfocus.core.database.entity.SyncStateEntity).\n"
                  + " Expected:\n" + _infoSyncState + "\n"
                  + " Found:\n" + _existingSyncState);
        }
        final HashMap<String, TableInfo.Column> _columnsScheduledTriggers = new HashMap<String, TableInfo.Column>(9);
        _columnsScheduledTriggers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("sourceRuleId", new TableInfo.Column("sourceRuleId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("sourceEventId", new TableInfo.Column("sourceEventId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("triggerAtMillis", new TableInfo.Column("triggerAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("profileId", new TableInfo.Column("profileId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("requestCode", new TableInfo.Column("requestCode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledTriggers.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduledTriggers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScheduledTriggers = new HashSet<TableInfo.Index>(2);
        _indicesScheduledTriggers.add(new TableInfo.Index("index_scheduled_triggers_sourceRuleId", false, Arrays.asList("sourceRuleId"), Arrays.asList("ASC")));
        _indicesScheduledTriggers.add(new TableInfo.Index("index_scheduled_triggers_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        final TableInfo _infoScheduledTriggers = new TableInfo("scheduled_triggers", _columnsScheduledTriggers, _foreignKeysScheduledTriggers, _indicesScheduledTriggers);
        final TableInfo _existingScheduledTriggers = TableInfo.read(db, "scheduled_triggers");
        if (!_infoScheduledTriggers.equals(_existingScheduledTriggers)) {
          return new RoomOpenHelper.ValidationResult(false, "scheduled_triggers(com.rebelfocus.core.database.entity.ScheduledTriggerEntity).\n"
                  + " Expected:\n" + _infoScheduledTriggers + "\n"
                  + " Found:\n" + _existingScheduledTriggers);
        }
        final HashMap<String, TableInfo.Column> _columnsDiagnosticLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsDiagnosticLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticLogs.put("level", new TableInfo.Column("level", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticLogs.put("component", new TableInfo.Column("component", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticLogs.put("message", new TableInfo.Column("message", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnosticLogs.put("extraData", new TableInfo.Column("extraData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiagnosticLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiagnosticLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiagnosticLogs = new TableInfo("diagnostic_logs", _columnsDiagnosticLogs, _foreignKeysDiagnosticLogs, _indicesDiagnosticLogs);
        final TableInfo _existingDiagnosticLogs = TableInfo.read(db, "diagnostic_logs");
        if (!_infoDiagnosticLogs.equals(_existingDiagnosticLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "diagnostic_logs(com.rebelfocus.core.database.entity.DiagnosticLogEntity).\n"
                  + " Expected:\n" + _infoDiagnosticLogs + "\n"
                  + " Found:\n" + _existingDiagnosticLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProgress = new HashMap<String, TableInfo.Column>(7);
        _columnsUserProgress.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("total_focus_time_millis", new TableInfo.Column("total_focus_time_millis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("total_sessions_completed", new TableInfo.Column("total_sessions_completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("current_streak_days", new TableInfo.Column("current_streak_days", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("longest_streak_days", new TableInfo.Column("longest_streak_days", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("last_session_date", new TableInfo.Column("last_session_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProgress = new TableInfo("user_progress", _columnsUserProgress, _foreignKeysUserProgress, _indicesUserProgress);
        final TableInfo _existingUserProgress = TableInfo.read(db, "user_progress");
        if (!_infoUserProgress.equals(_existingUserProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "user_progress(com.rebelfocus.core.database.entity.UserProgressEntity).\n"
                  + " Expected:\n" + _infoUserProgress + "\n"
                  + " Found:\n" + _existingUserProgress);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "69eb44fb0e448ba306086f1bfecefa00", "5c1bc2816412ce12f48a9c35eb1fff16");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "focus_profiles","blocked_apps","profile_blocked_app_cross_ref","sessions","automation_rules","audit_events","sync_state","scheduled_triggers","diagnostic_logs","user_progress");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `focus_profiles`");
      _db.execSQL("DELETE FROM `blocked_apps`");
      _db.execSQL("DELETE FROM `profile_blocked_app_cross_ref`");
      _db.execSQL("DELETE FROM `sessions`");
      _db.execSQL("DELETE FROM `automation_rules`");
      _db.execSQL("DELETE FROM `audit_events`");
      _db.execSQL("DELETE FROM `sync_state`");
      _db.execSQL("DELETE FROM `scheduled_triggers`");
      _db.execSQL("DELETE FROM `diagnostic_logs`");
      _db.execSQL("DELETE FROM `user_progress`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SessionDao.class, SessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BlockedAppDao.class, BlockedAppDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FocusProfileDao.class, FocusProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AutomationRuleDao.class, AutomationRuleDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProgressDao.class, UserProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AuditEventDao.class, AuditEventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncStateDao.class, SyncStateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduledTriggerDao.class, ScheduledTriggerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestoreDao.class, RestoreDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DiagnosticLogDao.class, DiagnosticLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SessionDao sessionDao() {
    if (_sessionDao != null) {
      return _sessionDao;
    } else {
      synchronized(this) {
        if(_sessionDao == null) {
          _sessionDao = new SessionDao_Impl(this);
        }
        return _sessionDao;
      }
    }
  }

  @Override
  public BlockedAppDao blockedAppDao() {
    if (_blockedAppDao != null) {
      return _blockedAppDao;
    } else {
      synchronized(this) {
        if(_blockedAppDao == null) {
          _blockedAppDao = new BlockedAppDao_Impl(this);
        }
        return _blockedAppDao;
      }
    }
  }

  @Override
  public FocusProfileDao focusProfileDao() {
    if (_focusProfileDao != null) {
      return _focusProfileDao;
    } else {
      synchronized(this) {
        if(_focusProfileDao == null) {
          _focusProfileDao = new FocusProfileDao_Impl(this);
        }
        return _focusProfileDao;
      }
    }
  }

  @Override
  public AutomationRuleDao automationRuleDao() {
    if (_automationRuleDao != null) {
      return _automationRuleDao;
    } else {
      synchronized(this) {
        if(_automationRuleDao == null) {
          _automationRuleDao = new AutomationRuleDao_Impl(this);
        }
        return _automationRuleDao;
      }
    }
  }

  @Override
  public UserProgressDao userProgressDao() {
    if (_userProgressDao != null) {
      return _userProgressDao;
    } else {
      synchronized(this) {
        if(_userProgressDao == null) {
          _userProgressDao = new UserProgressDao_Impl(this);
        }
        return _userProgressDao;
      }
    }
  }

  @Override
  public AuditEventDao auditEventDao() {
    if (_auditEventDao != null) {
      return _auditEventDao;
    } else {
      synchronized(this) {
        if(_auditEventDao == null) {
          _auditEventDao = new AuditEventDao_Impl(this);
        }
        return _auditEventDao;
      }
    }
  }

  @Override
  public SyncStateDao syncStateDao() {
    if (_syncStateDao != null) {
      return _syncStateDao;
    } else {
      synchronized(this) {
        if(_syncStateDao == null) {
          _syncStateDao = new SyncStateDao_Impl(this);
        }
        return _syncStateDao;
      }
    }
  }

  @Override
  public ScheduledTriggerDao scheduledTriggerDao() {
    if (_scheduledTriggerDao != null) {
      return _scheduledTriggerDao;
    } else {
      synchronized(this) {
        if(_scheduledTriggerDao == null) {
          _scheduledTriggerDao = new ScheduledTriggerDao_Impl(this);
        }
        return _scheduledTriggerDao;
      }
    }
  }

  @Override
  public RestoreDao restoreDao() {
    if (_restoreDao != null) {
      return _restoreDao;
    } else {
      synchronized(this) {
        if(_restoreDao == null) {
          _restoreDao = new RestoreDao_Impl(this);
        }
        return _restoreDao;
      }
    }
  }

  @Override
  public DiagnosticLogDao diagnosticLogDao() {
    if (_diagnosticLogDao != null) {
      return _diagnosticLogDao;
    } else {
      synchronized(this) {
        if(_diagnosticLogDao == null) {
          _diagnosticLogDao = new DiagnosticLogDao_Impl(this);
        }
        return _diagnosticLogDao;
      }
    }
  }
}
