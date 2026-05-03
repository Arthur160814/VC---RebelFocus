package com.rebelfocus.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.entity.AutomationRuleEntity;
import com.rebelfocus.core.database.entity.BlockedAppEntity;
import com.rebelfocus.core.database.entity.FocusProfileEntity;
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef;
import com.rebelfocus.core.model.CalendarMatchConfig;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RestoreDao_Impl implements RestoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FocusProfileEntity> __insertionAdapterOfFocusProfileEntity;

  private final EntityInsertionAdapter<BlockedAppEntity> __insertionAdapterOfBlockedAppEntity;

  private final EntityInsertionAdapter<AutomationRuleEntity> __insertionAdapterOfAutomationRuleEntity;

  private final EntityInsertionAdapter<ProfileBlockedAppCrossRef> __insertionAdapterOfProfileBlockedAppCrossRef;

  private final EntityDeletionOrUpdateAdapter<BlockedAppEntity> __updateAdapterOfBlockedAppEntity;

  public RestoreDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFocusProfileEntity = new EntityInsertionAdapter<FocusProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `focus_profiles` (`id`,`name`,`is_default`,`session_type`,`is_extreme_mode`,`is_ultimate_mode`,`focus_duration_millis`,`break_duration_millis`,`pomodoro_target`,`created_at`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FocusProfileEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        final int _tmp = entity.isDefault() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindString(4, entity.getSessionType());
        final int _tmp_1 = entity.isExtremeMode() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.isUltimateMode() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        statement.bindLong(7, entity.getFocusDurationMillis());
        statement.bindLong(8, entity.getBreakDurationMillis());
        statement.bindLong(9, entity.getPomodoroTarget());
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfBlockedAppEntity = new EntityInsertionAdapter<BlockedAppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `blocked_apps` (`package_name`,`app_name`,`is_enabled`,`created_at`,`updated_at`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedAppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getCreatedAt());
        statement.bindLong(5, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfAutomationRuleEntity = new EntityInsertionAdapter<AutomationRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `automation_rules` (`id`,`profile_id`,`name`,`is_enabled`,`schedule_type`,`start_time_minutes`,`end_time_minutes`,`days_of_week`,`created_at`,`updated_at`,`calendar_calendarId`,`calendar_titleKeyword`,`calendar_requireBusyStatus`,`calendar_timeWindowStartMinutes`,`calendar_timeWindowEndMinutes`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AutomationRuleEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getProfileId());
        statement.bindString(3, entity.getName());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getScheduleType());
        if (entity.getStartTimeMinutes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getStartTimeMinutes());
        }
        if (entity.getEndTimeMinutes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getEndTimeMinutes());
        }
        if (entity.getDaysOfWeek() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getDaysOfWeek());
        }
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        final CalendarMatchConfig _tmpCalendarMatchConfig = entity.getCalendarMatchConfig();
        if (_tmpCalendarMatchConfig != null) {
          statement.bindString(11, _tmpCalendarMatchConfig.getCalendarId());
          if (_tmpCalendarMatchConfig.getTitleKeyword() == null) {
            statement.bindNull(12);
          } else {
            statement.bindString(12, _tmpCalendarMatchConfig.getTitleKeyword());
          }
          final int _tmp_1 = _tmpCalendarMatchConfig.getRequireBusyStatus() ? 1 : 0;
          statement.bindLong(13, _tmp_1);
          if (_tmpCalendarMatchConfig.getTimeWindowStartMinutes() == null) {
            statement.bindNull(14);
          } else {
            statement.bindLong(14, _tmpCalendarMatchConfig.getTimeWindowStartMinutes());
          }
          if (_tmpCalendarMatchConfig.getTimeWindowEndMinutes() == null) {
            statement.bindNull(15);
          } else {
            statement.bindLong(15, _tmpCalendarMatchConfig.getTimeWindowEndMinutes());
          }
        } else {
          statement.bindNull(11);
          statement.bindNull(12);
          statement.bindNull(13);
          statement.bindNull(14);
          statement.bindNull(15);
        }
      }
    };
    this.__insertionAdapterOfProfileBlockedAppCrossRef = new EntityInsertionAdapter<ProfileBlockedAppCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `profile_blocked_app_cross_ref` (`profile_id`,`package_name`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProfileBlockedAppCrossRef entity) {
        statement.bindString(1, entity.getProfileId());
        statement.bindString(2, entity.getPackageName());
      }
    };
    this.__updateAdapterOfBlockedAppEntity = new EntityDeletionOrUpdateAdapter<BlockedAppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `blocked_apps` SET `package_name` = ?,`app_name` = ?,`is_enabled` = ?,`created_at` = ?,`updated_at` = ? WHERE `package_name` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BlockedAppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getAppName());
        final int _tmp = entity.isEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getCreatedAt());
        statement.bindLong(5, entity.getUpdatedAt());
        statement.bindString(6, entity.getPackageName());
      }
    };
  }

  @Override
  public Object insertProfile(final FocusProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFocusProfileEntity.insert(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBlockedApp(final BlockedAppEntity app,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBlockedAppEntity.insert(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertRule(final AutomationRuleEntity rule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAutomationRuleEntity.insert(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCrossRefs(final List<ProfileBlockedAppCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProfileBlockedAppCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBlockedApp(final BlockedAppEntity app,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBlockedAppEntity.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object restoreData(final List<FocusProfileEntity> profiles,
      final List<BlockedAppEntity> apps, final List<AutomationRuleEntity> rules,
      final List<ProfileBlockedAppCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> RestoreDao.DefaultImpls.restoreData(RestoreDao_Impl.this, profiles, apps, rules, crossRefs, __cont), $completion);
  }

  @Override
  public Object getProfileById(final String id,
      final Continuation<? super FocusProfileEntity> $completion) {
    final String _sql = "SELECT * FROM focus_profiles WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FocusProfileEntity>() {
      @Override
      @Nullable
      public FocusProfileEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "is_default");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfFocusDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "focus_duration_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final FocusProfileEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDefault);
            _tmpIsDefault = _tmp != 0;
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final boolean _tmpIsExtremeMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp_1 != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_2 != 0;
            final long _tmpFocusDurationMillis;
            _tmpFocusDurationMillis = _cursor.getLong(_cursorIndexOfFocusDurationMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new FocusProfileEntity(_tmpId,_tmpName,_tmpIsDefault,_tmpSessionType,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpFocusDurationMillis,_tmpBreakDurationMillis,_tmpPomodoroTarget,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBlockedApp(final String packageName,
      final Continuation<? super BlockedAppEntity> $completion) {
    final String _sql = "SELECT * FROM blocked_apps WHERE package_name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BlockedAppEntity>() {
      @Override
      @Nullable
      public BlockedAppEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "package_name");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "app_name");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final BlockedAppEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAppName;
            _tmpAppName = _cursor.getString(_cursorIndexOfAppName);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new BlockedAppEntity(_tmpPackageName,_tmpAppName,_tmpIsEnabled,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRuleById(final String id,
      final Continuation<? super AutomationRuleEntity> $completion) {
    final String _sql = "SELECT * FROM automation_rules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AutomationRuleEntity>() {
      @Override
      @Nullable
      public AutomationRuleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_enabled");
          final int _cursorIndexOfScheduleType = CursorUtil.getColumnIndexOrThrow(_cursor, "schedule_type");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_minutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time_minutes");
          final int _cursorIndexOfDaysOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "days_of_week");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCalendarId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_calendarId");
          final int _cursorIndexOfTitleKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_titleKeyword");
          final int _cursorIndexOfRequireBusyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_requireBusyStatus");
          final int _cursorIndexOfTimeWindowStartMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_timeWindowStartMinutes");
          final int _cursorIndexOfTimeWindowEndMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "calendar_timeWindowEndMinutes");
          final AutomationRuleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final boolean _tmpIsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsEnabled);
            _tmpIsEnabled = _tmp != 0;
            final String _tmpScheduleType;
            _tmpScheduleType = _cursor.getString(_cursorIndexOfScheduleType);
            final Integer _tmpStartTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfStartTimeMinutes)) {
              _tmpStartTimeMinutes = null;
            } else {
              _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            }
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpDaysOfWeek;
            if (_cursor.isNull(_cursorIndexOfDaysOfWeek)) {
              _tmpDaysOfWeek = null;
            } else {
              _tmpDaysOfWeek = _cursor.getString(_cursorIndexOfDaysOfWeek);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final CalendarMatchConfig _tmpCalendarMatchConfig;
            if (!(_cursor.isNull(_cursorIndexOfCalendarId) && _cursor.isNull(_cursorIndexOfTitleKeyword) && _cursor.isNull(_cursorIndexOfRequireBusyStatus) && _cursor.isNull(_cursorIndexOfTimeWindowStartMinutes) && _cursor.isNull(_cursorIndexOfTimeWindowEndMinutes))) {
              final String _tmpCalendarId;
              _tmpCalendarId = _cursor.getString(_cursorIndexOfCalendarId);
              final String _tmpTitleKeyword;
              if (_cursor.isNull(_cursorIndexOfTitleKeyword)) {
                _tmpTitleKeyword = null;
              } else {
                _tmpTitleKeyword = _cursor.getString(_cursorIndexOfTitleKeyword);
              }
              final boolean _tmpRequireBusyStatus;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfRequireBusyStatus);
              _tmpRequireBusyStatus = _tmp_1 != 0;
              final Integer _tmpTimeWindowStartMinutes;
              if (_cursor.isNull(_cursorIndexOfTimeWindowStartMinutes)) {
                _tmpTimeWindowStartMinutes = null;
              } else {
                _tmpTimeWindowStartMinutes = _cursor.getInt(_cursorIndexOfTimeWindowStartMinutes);
              }
              final Integer _tmpTimeWindowEndMinutes;
              if (_cursor.isNull(_cursorIndexOfTimeWindowEndMinutes)) {
                _tmpTimeWindowEndMinutes = null;
              } else {
                _tmpTimeWindowEndMinutes = _cursor.getInt(_cursorIndexOfTimeWindowEndMinutes);
              }
              _tmpCalendarMatchConfig = new CalendarMatchConfig(_tmpCalendarId,_tmpTitleKeyword,_tmpRequireBusyStatus,_tmpTimeWindowStartMinutes,_tmpTimeWindowEndMinutes);
            } else {
              _tmpCalendarMatchConfig = null;
            }
            _result = new AutomationRuleEntity(_tmpId,_tmpProfileId,_tmpName,_tmpIsEnabled,_tmpScheduleType,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpDaysOfWeek,_tmpCalendarMatchConfig,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCrossRefsForProfiles(final List<String> profileIds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM profile_blocked_app_cross_ref WHERE profile_id IN (");
        final int _inputSize = profileIds.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : profileIds) {
          _stmt.bindString(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
