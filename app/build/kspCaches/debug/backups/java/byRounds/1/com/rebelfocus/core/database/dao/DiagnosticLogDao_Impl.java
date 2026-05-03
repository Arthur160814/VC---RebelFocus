package com.rebelfocus.core.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.entity.DiagnosticLogEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class DiagnosticLogDao_Impl implements DiagnosticLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DiagnosticLogEntity> __insertionAdapterOfDiagnosticLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfPruneOldLogs;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public DiagnosticLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiagnosticLogEntity = new EntityInsertionAdapter<DiagnosticLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `diagnostic_logs` (`id`,`timestamp`,`level`,`component`,`message`,`extraData`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DiagnosticLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getLevel());
        statement.bindString(4, entity.getComponent());
        statement.bindString(5, entity.getMessage());
        if (entity.getExtraData() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExtraData());
        }
      }
    };
    this.__preparedStmtOfPruneOldLogs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM diagnostic_logs WHERE id NOT IN (SELECT id FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 200)";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM diagnostic_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DiagnosticLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDiagnosticLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object logAndPrune(final DiagnosticLogEntity log,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> DiagnosticLogDao.DefaultImpls.logAndPrune(DiagnosticLogDao_Impl.this, log, __cont), $completion);
  }

  @Override
  public Object pruneOldLogs(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfPruneOldLogs.acquire();
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
          __preparedStmtOfPruneOldLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DiagnosticLogEntity>> observeLatestLogs() {
    final String _sql = "SELECT * FROM diagnostic_logs ORDER BY timestamp DESC LIMIT 200";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"diagnostic_logs"}, new Callable<List<DiagnosticLogEntity>>() {
      @Override
      @NonNull
      public List<DiagnosticLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfComponent = CursorUtil.getColumnIndexOrThrow(_cursor, "component");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final List<DiagnosticLogEntity> _result = new ArrayList<DiagnosticLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DiagnosticLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpLevel;
            _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            final String _tmpComponent;
            _tmpComponent = _cursor.getString(_cursorIndexOfComponent);
            final String _tmpMessage;
            _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            _item = new DiagnosticLogEntity(_tmpId,_tmpTimestamp,_tmpLevel,_tmpComponent,_tmpMessage,_tmpExtraData);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
