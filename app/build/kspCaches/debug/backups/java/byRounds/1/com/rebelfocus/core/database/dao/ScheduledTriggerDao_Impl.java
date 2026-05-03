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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.Converters;
import com.rebelfocus.core.database.entity.ScheduledTriggerEntity;
import com.rebelfocus.core.model.ScheduledTriggerStatus;
import java.lang.Class;
import java.lang.Exception;
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
public final class ScheduledTriggerDao_Impl implements ScheduledTriggerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduledTriggerEntity> __insertionAdapterOfScheduledTriggerEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ScheduledTriggerEntity> __updateAdapterOfScheduledTriggerEntity;

  public ScheduledTriggerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduledTriggerEntity = new EntityInsertionAdapter<ScheduledTriggerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scheduled_triggers` (`id`,`sourceRuleId`,`sourceEventId`,`triggerAtMillis`,`profileId`,`status`,`requestCode`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledTriggerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSourceRuleId());
        if (entity.getSourceEventId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSourceEventId());
        }
        statement.bindLong(4, entity.getTriggerAtMillis());
        statement.bindString(5, entity.getProfileId());
        final String _tmp = __converters.fromScheduledTriggerStatus(entity.getStatus());
        statement.bindString(6, _tmp);
        statement.bindLong(7, entity.getRequestCode());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfScheduledTriggerEntity = new EntityDeletionOrUpdateAdapter<ScheduledTriggerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scheduled_triggers` SET `id` = ?,`sourceRuleId` = ?,`sourceEventId` = ?,`triggerAtMillis` = ?,`profileId` = ?,`status` = ?,`requestCode` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledTriggerEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSourceRuleId());
        if (entity.getSourceEventId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSourceEventId());
        }
        statement.bindLong(4, entity.getTriggerAtMillis());
        statement.bindString(5, entity.getProfileId());
        final String _tmp = __converters.fromScheduledTriggerStatus(entity.getStatus());
        statement.bindString(6, _tmp);
        statement.bindLong(7, entity.getRequestCode());
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        statement.bindString(10, entity.getId());
      }
    };
  }

  @Override
  public Object insertOrReplace(final ScheduledTriggerEntity trigger,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScheduledTriggerEntity.insert(trigger);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ScheduledTriggerEntity trigger,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduledTriggerEntity.handle(trigger);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id,
      final Continuation<? super ScheduledTriggerEntity> $completion) {
    final String _sql = "SELECT * FROM scheduled_triggers WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduledTriggerEntity>() {
      @Override
      @Nullable
      public ScheduledTriggerEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceRuleId");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfTriggerAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerAtMillis");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requestCode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ScheduledTriggerEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSourceRuleId;
            _tmpSourceRuleId = _cursor.getString(_cursorIndexOfSourceRuleId);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final long _tmpTriggerAtMillis;
            _tmpTriggerAtMillis = _cursor.getLong(_cursorIndexOfTriggerAtMillis);
            final String _tmpProfileId;
            _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            final ScheduledTriggerStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toScheduledTriggerStatus(_tmp);
            final int _tmpRequestCode;
            _tmpRequestCode = _cursor.getInt(_cursorIndexOfRequestCode);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ScheduledTriggerEntity(_tmpId,_tmpSourceRuleId,_tmpSourceEventId,_tmpTriggerAtMillis,_tmpProfileId,_tmpStatus,_tmpRequestCode,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getByRuleIdAndStatus(final String ruleId, final ScheduledTriggerStatus status,
      final Continuation<? super List<ScheduledTriggerEntity>> $completion) {
    final String _sql = "SELECT * FROM scheduled_triggers WHERE sourceRuleId = ? AND status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, ruleId);
    _argIndex = 2;
    final String _tmp = __converters.fromScheduledTriggerStatus(status);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduledTriggerEntity>>() {
      @Override
      @NonNull
      public List<ScheduledTriggerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceRuleId");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfTriggerAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerAtMillis");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requestCode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ScheduledTriggerEntity> _result = new ArrayList<ScheduledTriggerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduledTriggerEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSourceRuleId;
            _tmpSourceRuleId = _cursor.getString(_cursorIndexOfSourceRuleId);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final long _tmpTriggerAtMillis;
            _tmpTriggerAtMillis = _cursor.getLong(_cursorIndexOfTriggerAtMillis);
            final String _tmpProfileId;
            _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            final ScheduledTriggerStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toScheduledTriggerStatus(_tmp_1);
            final int _tmpRequestCode;
            _tmpRequestCode = _cursor.getInt(_cursorIndexOfRequestCode);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ScheduledTriggerEntity(_tmpId,_tmpSourceRuleId,_tmpSourceEventId,_tmpTriggerAtMillis,_tmpProfileId,_tmpStatus,_tmpRequestCode,_tmpCreatedAt,_tmpUpdatedAt);
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

  @Override
  public Flow<List<ScheduledTriggerEntity>> observeByStatus(final ScheduledTriggerStatus status) {
    final String _sql = "SELECT * FROM scheduled_triggers WHERE status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromScheduledTriggerStatus(status);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scheduled_triggers"}, new Callable<List<ScheduledTriggerEntity>>() {
      @Override
      @NonNull
      public List<ScheduledTriggerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSourceRuleId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceRuleId");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfTriggerAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "triggerAtMillis");
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profileId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequestCode = CursorUtil.getColumnIndexOrThrow(_cursor, "requestCode");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ScheduledTriggerEntity> _result = new ArrayList<ScheduledTriggerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduledTriggerEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSourceRuleId;
            _tmpSourceRuleId = _cursor.getString(_cursorIndexOfSourceRuleId);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final long _tmpTriggerAtMillis;
            _tmpTriggerAtMillis = _cursor.getLong(_cursorIndexOfTriggerAtMillis);
            final String _tmpProfileId;
            _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            final ScheduledTriggerStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toScheduledTriggerStatus(_tmp_1);
            final int _tmpRequestCode;
            _tmpRequestCode = _cursor.getInt(_cursorIndexOfRequestCode);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ScheduledTriggerEntity(_tmpId,_tmpSourceRuleId,_tmpSourceEventId,_tmpTriggerAtMillis,_tmpProfileId,_tmpStatus,_tmpRequestCode,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object updateStatuses(final List<String> ids, final ScheduledTriggerStatus newStatus,
      final long updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("UPDATE scheduled_triggers SET status = ");
        _stringBuilder.append("?");
        _stringBuilder.append(", updatedAt = ");
        _stringBuilder.append("?");
        _stringBuilder.append(" WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        final String _tmp = __converters.fromScheduledTriggerStatus(newStatus);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        for (String _item : ids) {
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
