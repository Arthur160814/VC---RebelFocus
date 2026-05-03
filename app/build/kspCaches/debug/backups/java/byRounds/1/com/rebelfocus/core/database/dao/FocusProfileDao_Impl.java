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
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rebelfocus.core.database.entity.BlockedAppEntity;
import com.rebelfocus.core.database.entity.FocusProfileEntity;
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef;
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
public final class FocusProfileDao_Impl implements FocusProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FocusProfileEntity> __insertionAdapterOfFocusProfileEntity;

  private final EntityInsertionAdapter<ProfileBlockedAppCrossRef> __insertionAdapterOfProfileBlockedAppCrossRef;

  private final EntityDeletionOrUpdateAdapter<FocusProfileEntity> __updateAdapterOfFocusProfileEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCrossRefsByProfileId;

  public FocusProfileDao_Impl(@NonNull final RoomDatabase __db) {
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
    this.__updateAdapterOfFocusProfileEntity = new EntityDeletionOrUpdateAdapter<FocusProfileEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `focus_profiles` SET `id` = ?,`name` = ?,`is_default` = ?,`session_type` = ?,`is_extreme_mode` = ?,`is_ultimate_mode` = ?,`focus_duration_millis` = ?,`break_duration_millis` = ?,`pomodoro_target` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
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
        statement.bindString(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM focus_profiles WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCrossRefsByProfileId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM profile_blocked_app_cross_ref WHERE profile_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FocusProfileEntity profile,
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
  public Object insertCrossRef(final ProfileBlockedAppCrossRef crossRef,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProfileBlockedAppCrossRef.insert(crossRef);
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
  public Object update(final FocusProfileEntity profile,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFocusProfileEntity.handle(profile);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceBlockedAppsForProfile(final String profileId,
      final List<String> packageNames, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> FocusProfileDao.DefaultImpls.replaceBlockedAppsForProfile(FocusProfileDao_Impl.this, profileId, packageNames, __cont), $completion);
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
  public Object deleteCrossRefsByProfileId(final String profileId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCrossRefsByProfileId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, profileId);
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
          __preparedStmtOfDeleteCrossRefsByProfileId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id,
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
  public Flow<FocusProfileEntity> observeById(final String id) {
    final String _sql = "SELECT * FROM focus_profiles WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_profiles"}, new Callable<FocusProfileEntity>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FocusProfileEntity>> observeAll() {
    final String _sql = "SELECT * FROM focus_profiles ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"focus_profiles"}, new Callable<List<FocusProfileEntity>>() {
      @Override
      @NonNull
      public List<FocusProfileEntity> call() throws Exception {
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
          final List<FocusProfileEntity> _result = new ArrayList<FocusProfileEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FocusProfileEntity _item;
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
            _item = new FocusProfileEntity(_tmpId,_tmpName,_tmpIsDefault,_tmpSessionType,_tmpIsExtremeMode,_tmpIsUltimateMode,_tmpFocusDurationMillis,_tmpBreakDurationMillis,_tmpPomodoroTarget,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getDefault(final Continuation<? super FocusProfileEntity> $completion) {
    final String _sql = "SELECT * FROM focus_profiles WHERE is_default = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public Flow<List<BlockedAppEntity>> observeBlockedAppsForProfile(final String profileId) {
    final String _sql = "\n"
            + "        SELECT ba.* FROM blocked_apps ba \n"
            + "        INNER JOIN profile_blocked_app_cross_ref ref ON ba.package_name = ref.package_name \n"
            + "        WHERE ref.profile_id = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, profileId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"blocked_apps",
        "profile_blocked_app_cross_ref"}, new Callable<List<BlockedAppEntity>>() {
      @Override
      @NonNull
      public List<BlockedAppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "package_name");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "app_name");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<BlockedAppEntity> _result = new ArrayList<BlockedAppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedAppEntity _item;
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
            _item = new BlockedAppEntity(_tmpPackageName,_tmpAppName,_tmpIsEnabled,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getBlockedAppsForProfile(final String profileId,
      final Continuation<? super List<BlockedAppEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT ba.* FROM blocked_apps ba \n"
            + "        INNER JOIN profile_blocked_app_cross_ref ref ON ba.package_name = ref.package_name \n"
            + "        WHERE ref.profile_id = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, profileId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BlockedAppEntity>>() {
      @Override
      @NonNull
      public List<BlockedAppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "package_name");
          final int _cursorIndexOfAppName = CursorUtil.getColumnIndexOrThrow(_cursor, "app_name");
          final int _cursorIndexOfIsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_enabled");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<BlockedAppEntity> _result = new ArrayList<BlockedAppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BlockedAppEntity _item;
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
            _item = new BlockedAppEntity(_tmpPackageName,_tmpAppName,_tmpIsEnabled,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM focus_profiles";
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
  public Object getAllCrossRefs(
      final Continuation<? super List<ProfileBlockedAppCrossRef>> $completion) {
    final String _sql = "SELECT * FROM profile_blocked_app_cross_ref";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProfileBlockedAppCrossRef>>() {
      @Override
      @NonNull
      public List<ProfileBlockedAppCrossRef> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProfileId = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "package_name");
          final List<ProfileBlockedAppCrossRef> _result = new ArrayList<ProfileBlockedAppCrossRef>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProfileBlockedAppCrossRef _item;
            final String _tmpProfileId;
            _tmpProfileId = _cursor.getString(_cursorIndexOfProfileId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            _item = new ProfileBlockedAppCrossRef(_tmpProfileId,_tmpPackageName);
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
