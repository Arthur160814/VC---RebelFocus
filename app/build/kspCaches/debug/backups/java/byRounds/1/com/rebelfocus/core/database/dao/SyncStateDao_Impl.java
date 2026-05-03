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
import com.rebelfocus.core.database.entity.SyncStateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class SyncStateDao_Impl implements SyncStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncStateEntity> __insertionAdapterOfSyncStateEntity;

  private final EntityDeletionOrUpdateAdapter<SyncStateEntity> __updateAdapterOfSyncStateEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public SyncStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncStateEntity = new EntityInsertionAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sync_state` (`id`,`entity_type`,`entity_id`,`last_sync_timestamp`,`last_sync_status`,`sync_version`,`updated_at`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEntityType());
        if (entity.getEntityId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEntityId());
        }
        if (entity.getLastSyncTimestamp() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getLastSyncTimestamp());
        }
        if (entity.getLastSyncStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLastSyncStatus());
        }
        statement.bindLong(6, entity.getSyncVersion());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfSyncStateEntity = new EntityDeletionOrUpdateAdapter<SyncStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sync_state` SET `id` = ?,`entity_type` = ?,`entity_id` = ?,`last_sync_timestamp` = ?,`last_sync_status` = ?,`sync_version` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStateEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEntityType());
        if (entity.getEntityId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEntityId());
        }
        if (entity.getLastSyncTimestamp() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getLastSyncTimestamp());
        }
        if (entity.getLastSyncStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLastSyncStatus());
        }
        statement.bindLong(6, entity.getSyncVersion());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindString(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM sync_state WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStateEntity.insert(syncState);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SyncStateEntity syncState,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSyncStateEntity.handle(syncState);
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
  public Object getByEntityType(final String entityType,
      final Continuation<? super SyncStateEntity> $completion) {
    final String _sql = "SELECT * FROM sync_state WHERE entity_type = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, entityType);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncStateEntity>() {
      @Override
      @Nullable
      public SyncStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entity_type");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entity_id");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync_timestamp");
          final int _cursorIndexOfLastSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync_status");
          final int _cursorIndexOfSyncVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_version");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final SyncStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final String _tmpEntityId;
            if (_cursor.isNull(_cursorIndexOfEntityId)) {
              _tmpEntityId = null;
            } else {
              _tmpEntityId = _cursor.getString(_cursorIndexOfEntityId);
            }
            final Long _tmpLastSyncTimestamp;
            if (_cursor.isNull(_cursorIndexOfLastSyncTimestamp)) {
              _tmpLastSyncTimestamp = null;
            } else {
              _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            }
            final String _tmpLastSyncStatus;
            if (_cursor.isNull(_cursorIndexOfLastSyncStatus)) {
              _tmpLastSyncStatus = null;
            } else {
              _tmpLastSyncStatus = _cursor.getString(_cursorIndexOfLastSyncStatus);
            }
            final int _tmpSyncVersion;
            _tmpSyncVersion = _cursor.getInt(_cursorIndexOfSyncVersion);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new SyncStateEntity(_tmpId,_tmpEntityType,_tmpEntityId,_tmpLastSyncTimestamp,_tmpLastSyncStatus,_tmpSyncVersion,_tmpUpdatedAt);
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
  public Flow<List<SyncStateEntity>> observeAll() {
    final String _sql = "SELECT * FROM sync_state ORDER BY updated_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sync_state"}, new Callable<List<SyncStateEntity>>() {
      @Override
      @NonNull
      public List<SyncStateEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entity_type");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entity_id");
          final int _cursorIndexOfLastSyncTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync_timestamp");
          final int _cursorIndexOfLastSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "last_sync_status");
          final int _cursorIndexOfSyncVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_version");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<SyncStateEntity> _result = new ArrayList<SyncStateEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SyncStateEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final String _tmpEntityId;
            if (_cursor.isNull(_cursorIndexOfEntityId)) {
              _tmpEntityId = null;
            } else {
              _tmpEntityId = _cursor.getString(_cursorIndexOfEntityId);
            }
            final Long _tmpLastSyncTimestamp;
            if (_cursor.isNull(_cursorIndexOfLastSyncTimestamp)) {
              _tmpLastSyncTimestamp = null;
            } else {
              _tmpLastSyncTimestamp = _cursor.getLong(_cursorIndexOfLastSyncTimestamp);
            }
            final String _tmpLastSyncStatus;
            if (_cursor.isNull(_cursorIndexOfLastSyncStatus)) {
              _tmpLastSyncStatus = null;
            } else {
              _tmpLastSyncStatus = _cursor.getString(_cursorIndexOfLastSyncStatus);
            }
            final int _tmpSyncVersion;
            _tmpSyncVersion = _cursor.getInt(_cursorIndexOfSyncVersion);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new SyncStateEntity(_tmpId,_tmpEntityType,_tmpEntityId,_tmpLastSyncTimestamp,_tmpLastSyncStatus,_tmpSyncVersion,_tmpUpdatedAt);
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
