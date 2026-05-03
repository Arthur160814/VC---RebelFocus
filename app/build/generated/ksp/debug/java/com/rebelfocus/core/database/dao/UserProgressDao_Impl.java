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
import com.rebelfocus.core.database.entity.UserProgressEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserProgressDao_Impl implements UserProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProgressEntity> __insertionAdapterOfUserProgressEntity;

  private final EntityDeletionOrUpdateAdapter<UserProgressEntity> __updateAdapterOfUserProgressEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public UserProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProgressEntity = new EntityInsertionAdapter<UserProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_progress` (`id`,`total_focus_time_millis`,`total_sessions_completed`,`current_streak_days`,`longest_streak_days`,`last_session_date`,`updated_at`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProgressEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getTotalFocusTimeMillis());
        statement.bindLong(3, entity.getTotalSessionsCompleted());
        statement.bindLong(4, entity.getCurrentStreakDays());
        statement.bindLong(5, entity.getLongestStreakDays());
        if (entity.getLastSessionDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastSessionDate());
        }
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfUserProgressEntity = new EntityDeletionOrUpdateAdapter<UserProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_progress` SET `id` = ?,`total_focus_time_millis` = ?,`total_sessions_completed` = ?,`current_streak_days` = ?,`longest_streak_days` = ?,`last_session_date` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserProgressEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getTotalFocusTimeMillis());
        statement.bindLong(3, entity.getTotalSessionsCompleted());
        statement.bindLong(4, entity.getCurrentStreakDays());
        statement.bindLong(5, entity.getLongestStreakDays());
        if (entity.getLastSessionDate() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getLastSessionDate());
        }
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindString(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_progress";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UserProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserProgressEntity.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UserProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserProgressEntity.handle(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object get(final Continuation<? super UserProgressEntity> $completion) {
    final String _sql = "SELECT * FROM user_progress LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserProgressEntity>() {
      @Override
      @Nullable
      public UserProgressEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTotalFocusTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "total_focus_time_millis");
          final int _cursorIndexOfTotalSessionsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "total_sessions_completed");
          final int _cursorIndexOfCurrentStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak_days");
          final int _cursorIndexOfLongestStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak_days");
          final int _cursorIndexOfLastSessionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "last_session_date");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final UserProgressEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpTotalFocusTimeMillis;
            _tmpTotalFocusTimeMillis = _cursor.getLong(_cursorIndexOfTotalFocusTimeMillis);
            final int _tmpTotalSessionsCompleted;
            _tmpTotalSessionsCompleted = _cursor.getInt(_cursorIndexOfTotalSessionsCompleted);
            final int _tmpCurrentStreakDays;
            _tmpCurrentStreakDays = _cursor.getInt(_cursorIndexOfCurrentStreakDays);
            final int _tmpLongestStreakDays;
            _tmpLongestStreakDays = _cursor.getInt(_cursorIndexOfLongestStreakDays);
            final Long _tmpLastSessionDate;
            if (_cursor.isNull(_cursorIndexOfLastSessionDate)) {
              _tmpLastSessionDate = null;
            } else {
              _tmpLastSessionDate = _cursor.getLong(_cursorIndexOfLastSessionDate);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UserProgressEntity(_tmpId,_tmpTotalFocusTimeMillis,_tmpTotalSessionsCompleted,_tmpCurrentStreakDays,_tmpLongestStreakDays,_tmpLastSessionDate,_tmpUpdatedAt);
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
  public Flow<UserProgressEntity> observe() {
    final String _sql = "SELECT * FROM user_progress LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_progress"}, new Callable<UserProgressEntity>() {
      @Override
      @Nullable
      public UserProgressEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTotalFocusTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "total_focus_time_millis");
          final int _cursorIndexOfTotalSessionsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "total_sessions_completed");
          final int _cursorIndexOfCurrentStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "current_streak_days");
          final int _cursorIndexOfLongestStreakDays = CursorUtil.getColumnIndexOrThrow(_cursor, "longest_streak_days");
          final int _cursorIndexOfLastSessionDate = CursorUtil.getColumnIndexOrThrow(_cursor, "last_session_date");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final UserProgressEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpTotalFocusTimeMillis;
            _tmpTotalFocusTimeMillis = _cursor.getLong(_cursorIndexOfTotalFocusTimeMillis);
            final int _tmpTotalSessionsCompleted;
            _tmpTotalSessionsCompleted = _cursor.getInt(_cursorIndexOfTotalSessionsCompleted);
            final int _tmpCurrentStreakDays;
            _tmpCurrentStreakDays = _cursor.getInt(_cursorIndexOfCurrentStreakDays);
            final int _tmpLongestStreakDays;
            _tmpLongestStreakDays = _cursor.getInt(_cursorIndexOfLongestStreakDays);
            final Long _tmpLastSessionDate;
            if (_cursor.isNull(_cursorIndexOfLastSessionDate)) {
              _tmpLastSessionDate = null;
            } else {
              _tmpLastSessionDate = _cursor.getLong(_cursorIndexOfLastSessionDate);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UserProgressEntity(_tmpId,_tmpTotalFocusTimeMillis,_tmpTotalSessionsCompleted,_tmpCurrentStreakDays,_tmpLongestStreakDays,_tmpLastSessionDate,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
