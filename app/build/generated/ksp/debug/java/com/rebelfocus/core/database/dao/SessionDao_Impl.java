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
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.entity.SessionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SessionEntity> __insertionAdapterOfSessionEntity;

  private final EntityDeletionOrUpdateAdapter<SessionEntity> __updateAdapterOfSessionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public SessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSessionEntity = new EntityInsertionAdapter<SessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sessions` (`id`,`profile_id`,`session_type`,`state`,`start_time_elapsed_realtime`,`planned_duration_millis`,`elapsed_at_pause_millis`,`break_duration_millis`,`pomodoro_count`,`pomodoro_target`,`is_extreme_mode`,`is_ultimate_mode`,`created_at`,`updated_at`,`completed_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SessionEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getProfileId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProfileId());
        }
        statement.bindString(3, entity.getSessionType());
        statement.bindString(4, entity.getState());
        if (entity.getStartTimeElapsedRealtime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getStartTimeElapsedRealtime());
        }
        statement.bindLong(6, entity.getPlannedDurationMillis());
        statement.bindLong(7, entity.getElapsedAtPauseMillis());
        statement.bindLong(8, entity.getBreakDurationMillis());
        statement.bindLong(9, entity.getPomodoroCount());
        statement.bindLong(10, entity.getPomodoroTarget());
        final int _tmp = entity.isExtremeMode() ? 1 : 0;
        statement.bindLong(11, _tmp);
        final int _tmp_1 = entity.isUltimateMode() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCompletedAt());
        }
      }
    };
    this.__updateAdapterOfSessionEntity = new EntityDeletionOrUpdateAdapter<SessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sessions` SET `id` = ?,`profile_id` = ?,`session_type` = ?,`state` = ?,`start_time_elapsed_realtime` = ?,`planned_duration_millis` = ?,`elapsed_at_pause_millis` = ?,`break_duration_millis` = ?,`pomodoro_count` = ?,`pomodoro_target` = ?,`is_extreme_mode` = ?,`is_ultimate_mode` = ?,`created_at` = ?,`updated_at` = ?,`completed_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SessionEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getProfileId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getProfileId());
        }
        statement.bindString(3, entity.getSessionType());
        statement.bindString(4, entity.getState());
        if (entity.getStartTimeElapsedRealtime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getStartTimeElapsedRealtime());
        }
        statement.bindLong(6, entity.getPlannedDurationMillis());
        statement.bindLong(7, entity.getElapsedAtPauseMillis());
        statement.bindLong(8, entity.getBreakDurationMillis());
        statement.bindLong(9, entity.getPomodoroCount());
        statement.bindLong(10, entity.getPomodoroTarget());
        final int _tmp = entity.isExtremeMode() ? 1 : 0;
        statement.bindLong(11, _tmp);
        final int _tmp_1 = entity.isUltimateMode() ? 1 : 0;
        statement.bindLong(12, _tmp_1);
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCompletedAt());
        }
        statement.bindString(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sessions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SessionEntity session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SessionEntity session, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSessionEntity.handle(session);
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
  public Object getById(final String id, final Continuation<? super SessionEntity> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SessionEntity>() {
      @Override
      @Nullable
      public SessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final SessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
  public Flow<SessionEntity> observeById(final String id) {
    final String _sql = "SELECT * FROM sessions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<SessionEntity>() {
      @Override
      @Nullable
      public SessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final SessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
          } else {
            _result = null;
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
  public Flow<List<SessionEntity>> observeAll() {
    final String _sql = "SELECT * FROM sessions ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
  public Flow<List<SessionEntity>> observeByStates(final List<String> states) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM sessions WHERE state IN (");
    final int _inputSize = states.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY updated_at DESC");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : states) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item_1 = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
            _result.add(_item_1);
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
  public Flow<SessionEntity> observeActiveSession(final List<String> states) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM sessions WHERE state IN (");
    final int _inputSize = states.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY updated_at DESC LIMIT 1");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : states) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<SessionEntity>() {
      @Override
      @Nullable
      public SessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final SessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
          } else {
            _result = null;
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
  public Object getActiveSession(final List<String> states,
      final Continuation<? super SessionEntity> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM sessions WHERE state IN (");
    final int _inputSize = states.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") ORDER BY updated_at DESC LIMIT 1");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : states) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SessionEntity>() {
      @Override
      @Nullable
      public SessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final SessionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM sessions";
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

  @Override
  public Flow<List<SessionEntity>> observeCompletedSessions() {
    final String _sql = "SELECT * FROM sessions WHERE state = 'Completed' ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sessions"}, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
  public Object getCompletedSessionsSince(final long sinceMillis,
      final Continuation<? super List<SessionEntity>> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE state = 'Completed' AND created_at >= ? ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sinceMillis);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SessionEntity>>() {
      @Override
      @NonNull
      public List<SessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfSessionType = CursorUtil.getColumnIndexOrThrow(_cursor, "session_type");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfStartTimeElapsedRealtime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time_elapsed_realtime");
          final int _cursorIndexOfPlannedDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "planned_duration_millis");
          final int _cursorIndexOfElapsedAtPauseMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "elapsed_at_pause_millis");
          final int _cursorIndexOfBreakDurationMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "break_duration_millis");
          final int _cursorIndexOfPomodoroCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_count");
          final int _cursorIndexOfPomodoroTarget = CursorUtil.getColumnIndexOrThrow(_cursor, "pomodoro_target");
          final int _cursorIndexOfIsExtremeMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_extreme_mode");
          final int _cursorIndexOfIsUltimateMode = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ultimate_mode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completed_at");
          final List<SessionEntity> _result = new ArrayList<SessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SessionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpProfileId;
            if (_cursor.isNull(_cursorIndexOfProfileId)) {
              _tmpProfileId = null;
            } else {
              _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            }
            final String _tmpSessionType;
            _tmpSessionType = _cursor.getString(_cursorIndexOfSessionType);
            final String _tmpState;
            _tmpState = _cursor.getString(_cursorIndexOfState);
            final Long _tmpStartTimeElapsedRealtime;
            if (_cursor.isNull(_cursorIndexOfStartTimeElapsedRealtime)) {
              _tmpStartTimeElapsedRealtime = null;
            } else {
              _tmpStartTimeElapsedRealtime = _cursor.getLong(_cursorIndexOfStartTimeElapsedRealtime);
            }
            final long _tmpPlannedDurationMillis;
            _tmpPlannedDurationMillis = _cursor.getLong(_cursorIndexOfPlannedDurationMillis);
            final long _tmpElapsedAtPauseMillis;
            _tmpElapsedAtPauseMillis = _cursor.getLong(_cursorIndexOfElapsedAtPauseMillis);
            final long _tmpBreakDurationMillis;
            _tmpBreakDurationMillis = _cursor.getLong(_cursorIndexOfBreakDurationMillis);
            final int _tmpPomodoroCount;
            _tmpPomodoroCount = _cursor.getInt(_cursorIndexOfPomodoroCount);
            final int _tmpPomodoroTarget;
            _tmpPomodoroTarget = _cursor.getInt(_cursorIndexOfPomodoroTarget);
            final boolean _tmpIsExtremeMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsExtremeMode);
            _tmpIsExtremeMode = _tmp != 0;
            final boolean _tmpIsUltimateMode;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsUltimateMode);
            _tmpIsUltimateMode = _tmp_1 != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new SessionEntity(_tmpId,_tmpProfileId,_tmpSessionType,_tmpState,_tmpStartTimeElapsedRealtime,_tmpPlannedDurationMillis,_tmpElapsedAtPauseMillis,_tmpBreakDurationMillis,_tmpPomodoroCount,_tmpPomodoroTarget,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
            _result.add(_item);
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
