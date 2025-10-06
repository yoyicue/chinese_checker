package com.yoyicue.chinesechecker.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SaveGameDao_Impl implements SaveGameDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SaveGameEntity> __insertionAdapterOfSaveGameEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public SaveGameDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSaveGameEntity = new EntityInsertionAdapter<SaveGameEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `save_game` (`id`,`updatedAt`,`playerCount`,`currentPlayer`,`occupantJson`,`lastMoveJson`,`lastOwner`,`playersJson`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SaveGameEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUpdatedAt());
        statement.bindLong(3, entity.getPlayerCount());
        if (entity.getCurrentPlayer() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCurrentPlayer());
        }
        if (entity.getOccupantJson() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOccupantJson());
        }
        if (entity.getLastMoveJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastMoveJson());
        }
        if (entity.getLastOwner() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLastOwner());
        }
        if (entity.getPlayersJson() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPlayersJson());
        }
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM save_game WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final SaveGameEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSaveGameEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
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
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object get(final Continuation<? super SaveGameEntity> $completion) {
    final String _sql = "SELECT * FROM save_game WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SaveGameEntity>() {
      @Override
      @Nullable
      public SaveGameEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPlayerCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playerCount");
          final int _cursorIndexOfCurrentPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPlayer");
          final int _cursorIndexOfOccupantJson = CursorUtil.getColumnIndexOrThrow(_cursor, "occupantJson");
          final int _cursorIndexOfLastMoveJson = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMoveJson");
          final int _cursorIndexOfLastOwner = CursorUtil.getColumnIndexOrThrow(_cursor, "lastOwner");
          final int _cursorIndexOfPlayersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "playersJson");
          final SaveGameEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpPlayerCount;
            _tmpPlayerCount = _cursor.getInt(_cursorIndexOfPlayerCount);
            final String _tmpCurrentPlayer;
            if (_cursor.isNull(_cursorIndexOfCurrentPlayer)) {
              _tmpCurrentPlayer = null;
            } else {
              _tmpCurrentPlayer = _cursor.getString(_cursorIndexOfCurrentPlayer);
            }
            final String _tmpOccupantJson;
            if (_cursor.isNull(_cursorIndexOfOccupantJson)) {
              _tmpOccupantJson = null;
            } else {
              _tmpOccupantJson = _cursor.getString(_cursorIndexOfOccupantJson);
            }
            final String _tmpLastMoveJson;
            if (_cursor.isNull(_cursorIndexOfLastMoveJson)) {
              _tmpLastMoveJson = null;
            } else {
              _tmpLastMoveJson = _cursor.getString(_cursorIndexOfLastMoveJson);
            }
            final String _tmpLastOwner;
            if (_cursor.isNull(_cursorIndexOfLastOwner)) {
              _tmpLastOwner = null;
            } else {
              _tmpLastOwner = _cursor.getString(_cursorIndexOfLastOwner);
            }
            final String _tmpPlayersJson;
            if (_cursor.isNull(_cursorIndexOfPlayersJson)) {
              _tmpPlayersJson = null;
            } else {
              _tmpPlayersJson = _cursor.getString(_cursorIndexOfPlayersJson);
            }
            _result = new SaveGameEntity(_tmpId,_tmpUpdatedAt,_tmpPlayerCount,_tmpCurrentPlayer,_tmpOccupantJson,_tmpLastMoveJson,_tmpLastOwner,_tmpPlayersJson);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
