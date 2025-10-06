package com.yoyicue.chinesechecker.data.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nH\'J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\nH\'J\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\nH\'J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\nH\'J\u0014\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\nH\'\u00a8\u0006\u0013"}, d2 = {"Lcom/yoyicue/chinesechecker/data/db/GameResultDao;", "", "clearAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "entity", "Lcom/yoyicue/chinesechecker/data/db/GameResultEntity;", "(Lcom/yoyicue/chinesechecker/data/db/GameResultEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "lastPlayed", "Lkotlinx/coroutines/flow/Flow;", "", "totalGames", "", "winsByController", "", "Lcom/yoyicue/chinesechecker/data/db/WinnerCount;", "winsByDifficulty", "winsByPlayer", "app_debug"})
@androidx.room.Dao()
public abstract interface GameResultDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.data.db.GameResultEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM game_results")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM game_results")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> totalGames();
    
    @androidx.room.Query(value = "SELECT MAX(timestamp) FROM game_results")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Long> lastPlayed();
    
    @androidx.room.Query(value = "SELECT winnerPlayer AS label, COUNT(*) AS count FROM game_results WHERE winnerPlayer IS NOT NULL GROUP BY winnerPlayer")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.yoyicue.chinesechecker.data.db.WinnerCount>> winsByPlayer();
    
    @androidx.room.Query(value = "SELECT winnerController AS label, COUNT(*) AS count FROM game_results WHERE winnerController IS NOT NULL GROUP BY winnerController")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.yoyicue.chinesechecker.data.db.WinnerCount>> winsByController();
    
    @androidx.room.Query(value = "SELECT winnerDifficulty AS label, COUNT(*) AS count FROM game_results WHERE winnerDifficulty IS NOT NULL GROUP BY winnerDifficulty")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.yoyicue.chinesechecker.data.db.WinnerCount>> winsByDifficulty();
}