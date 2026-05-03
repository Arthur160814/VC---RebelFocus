package com.rebelfocus.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.entity.AutomationRuleEntity;
import com.rebelfocus.core.model.CalendarMatchConfig;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AutomationRuleDao_Impl implements AutomationRuleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AutomationRuleEntity> __insertionAdapterOfAutomationRuleEntity;

  private final EntityDeletionOrUpdateAdapter<AutomationRuleEntity> __updateAdapterOfAutomationRuleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public AutomationRuleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
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
    this.__updateAdapterOfAutomationRuleEntity = new EntityDeletionOrUpdateAdapter<AutomationRuleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `automation_rules` SET `id` = ?,`profile_id` = ?,`name` = ?,`is_enabled` = ?,`schedule_type` = ?,`start_time_minutes` = ?,`end_time_minutes` = ?,`days_of_week` = ?,`created_at` = ?,`updated_at` = ?,`calendar_calendarId` = ?,`calendar_titleKeyword` = ?,`calendar_requireBusyStatus` = ?,`calendar_timeWindowStartMinutes` = ?,`calendar_timeWindowEndMinutes` = ? WHERE `id` = ?";
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
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM automation_rules WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AutomationRuleEntity rule,
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
  public Object update(final AutomationRuleEntity rule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAutomationRuleEntity.handle(rule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id,
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
  public Flow<List<AutomationRuleEntity>> observeAll() {
    final String _sql = "SELECT * FROM automation_rules ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"automation_rules"}, new Callable<List<AutomationRuleEntity>>() {
      @Override
      @NonNull
      public List<AutomationRuleEntity> call() throws Exception {
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
          final List<AutomationRuleEntity> _result = new ArrayList<AutomationRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AutomationRuleEntity _item;
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
            _item = new AutomationRuleEntity(_tmpId,_tmpProfileId,_tmpName,_tmpIsEnabled,_tmpScheduleType,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpDaysOfWeek,_tmpCalendarMatchConfig,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AutomationRuleEntity>> observeEnabled() {
    final String _sql = "SELECT * FROM automation_rules WHERE is_enabled = 1 ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"automation_rules"}, new Callable<List<AutomationRuleEntity>>() {
      @Override
      @NonNull
      public List<AutomationRuleEntity> call() throws Exception {
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
          final List<AutomationRuleEntity> _result = new ArrayList<AutomationRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AutomationRuleEntity _item;
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
            _item = new AutomationRuleEntity(_tmpId,_tmpProfileId,_tmpName,_tmpIsEnabled,_tmpScheduleType,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpDaysOfWeek,_tmpCalendarMatchConfig,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<AutomationRuleEntity>> observeByProfileId(final String profileId) {
    final String _sql = "SELECT * FROM automation_rules WHERE profile_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, profileId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"automation_rules"}, new Callable<List<AutomationRuleEntity>>() {
      @Override
      @NonNull
      public List<AutomationRuleEntity> call() throws Exception {
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
          final List<AutomationRuleEntity> _result = new ArrayList<AutomationRuleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AutomationRuleEntity _item;
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
            _item = new AutomationRuleEntity(_tmpId,_tmpProfileId,_tmpName,_tmpIsEnabled,_tmpScheduleType,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpDaysOfWeek,_tmpCalendarMatchConfig,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM automation_rules";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
