package com.yoyicue.chinesechecker.data.db;

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
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProfileDao _profileDao;

  private volatile GameResultDao _gameResultDao;

  private volatile SaveGameDao _saveGameDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`id` INTEGER NOT NULL, `nickname` TEXT NOT NULL, `avatarUri` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `game_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `playerCount` INTEGER NOT NULL, `winnerPlayer` TEXT, `winnerController` TEXT, `winnerDifficulty` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `save_game` (`id` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `playerCount` INTEGER NOT NULL, `currentPlayer` TEXT NOT NULL, `occupantJson` TEXT NOT NULL, `lastMoveJson` TEXT, `lastOwner` TEXT, `playersJson` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c953acffea1d2fb7181e26c4df96a7c9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `profiles`");
        db.execSQL("DROP TABLE IF EXISTS `game_results`");
        db.execSQL("DROP TABLE IF EXISTS `save_game`");
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
        final HashMap<String, TableInfo.Column> _columnsProfiles = new HashMap<String, TableInfo.Column>(3);
        _columnsProfiles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("nickname", new TableInfo.Column("nickname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProfiles.put("avatarUri", new TableInfo.Column("avatarUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProfiles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProfiles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProfiles = new TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles, _indicesProfiles);
        final TableInfo _existingProfiles = TableInfo.read(db, "profiles");
        if (!_infoProfiles.equals(_existingProfiles)) {
          return new RoomOpenHelper.ValidationResult(false, "profiles(com.yoyicue.chinesechecker.data.db.ProfileEntity).\n"
                  + " Expected:\n" + _infoProfiles + "\n"
                  + " Found:\n" + _existingProfiles);
        }
        final HashMap<String, TableInfo.Column> _columnsGameResults = new HashMap<String, TableInfo.Column>(6);
        _columnsGameResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameResults.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameResults.put("playerCount", new TableInfo.Column("playerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameResults.put("winnerPlayer", new TableInfo.Column("winnerPlayer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameResults.put("winnerController", new TableInfo.Column("winnerController", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameResults.put("winnerDifficulty", new TableInfo.Column("winnerDifficulty", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGameResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGameResults = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGameResults = new TableInfo("game_results", _columnsGameResults, _foreignKeysGameResults, _indicesGameResults);
        final TableInfo _existingGameResults = TableInfo.read(db, "game_results");
        if (!_infoGameResults.equals(_existingGameResults)) {
          return new RoomOpenHelper.ValidationResult(false, "game_results(com.yoyicue.chinesechecker.data.db.GameResultEntity).\n"
                  + " Expected:\n" + _infoGameResults + "\n"
                  + " Found:\n" + _existingGameResults);
        }
        final HashMap<String, TableInfo.Column> _columnsSaveGame = new HashMap<String, TableInfo.Column>(8);
        _columnsSaveGame.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("playerCount", new TableInfo.Column("playerCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("currentPlayer", new TableInfo.Column("currentPlayer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("occupantJson", new TableInfo.Column("occupantJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("lastMoveJson", new TableInfo.Column("lastMoveJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("lastOwner", new TableInfo.Column("lastOwner", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSaveGame.put("playersJson", new TableInfo.Column("playersJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSaveGame = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSaveGame = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSaveGame = new TableInfo("save_game", _columnsSaveGame, _foreignKeysSaveGame, _indicesSaveGame);
        final TableInfo _existingSaveGame = TableInfo.read(db, "save_game");
        if (!_infoSaveGame.equals(_existingSaveGame)) {
          return new RoomOpenHelper.ValidationResult(false, "save_game(com.yoyicue.chinesechecker.data.db.SaveGameEntity).\n"
                  + " Expected:\n" + _infoSaveGame + "\n"
                  + " Found:\n" + _existingSaveGame);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c953acffea1d2fb7181e26c4df96a7c9", "61488891c498d347cd9eecfe67c3e5a6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "profiles","game_results","save_game");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `profiles`");
      _db.execSQL("DELETE FROM `game_results`");
      _db.execSQL("DELETE FROM `save_game`");
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
    _typeConvertersMap.put(ProfileDao.class, ProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GameResultDao.class, GameResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SaveGameDao.class, SaveGameDao_Impl.getRequiredConverters());
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
  public ProfileDao profileDao() {
    if (_profileDao != null) {
      return _profileDao;
    } else {
      synchronized(this) {
        if(_profileDao == null) {
          _profileDao = new ProfileDao_Impl(this);
        }
        return _profileDao;
      }
    }
  }

  @Override
  public GameResultDao gameResultDao() {
    if (_gameResultDao != null) {
      return _gameResultDao;
    } else {
      synchronized(this) {
        if(_gameResultDao == null) {
          _gameResultDao = new GameResultDao_Impl(this);
        }
        return _gameResultDao;
      }
    }
  }

  @Override
  public SaveGameDao saveGameDao() {
    if (_saveGameDao != null) {
      return _saveGameDao;
    } else {
      synchronized(this) {
        if(_saveGameDao == null) {
          _saveGameDao = new SaveGameDao_Impl(this);
        }
        return _saveGameDao;
      }
    }
  }
}
