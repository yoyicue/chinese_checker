package com.yoyicue.chinesechecker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J*\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\u0006H\u0086@\u00a2\u0006\u0002\u0010\u000fJ\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/yoyicue/chinesechecker/data/StatsRepository;", "", "dao", "Lcom/yoyicue/chinesechecker/data/db/GameResultDao;", "(Lcom/yoyicue/chinesechecker/data/db/GameResultDao;)V", "recordGameResult", "", "winner", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "config", "Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "whenMillis", "", "(Lcom/yoyicue/chinesechecker/game/Board$PlayerId;Lcom/yoyicue/chinesechecker/ui/game/GameConfig;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reset", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "stats", "Lkotlinx/coroutines/flow/Flow;", "Lcom/yoyicue/chinesechecker/data/StatsUi;", "app_debug"})
public final class StatsRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.db.GameResultDao dao = null;
    
    public StatsRepository(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.data.db.GameResultDao dao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object recordGameResult(@org.jetbrains.annotations.Nullable()
    com.yoyicue.chinesechecker.game.Board.PlayerId winner, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.ui.game.GameConfig config, long whenMillis, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.yoyicue.chinesechecker.data.StatsUi> stats() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object reset(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}