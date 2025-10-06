package com.yoyicue.chinesechecker.game;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/yoyicue/chinesechecker/game/WeakBot;", "Lcom/yoyicue/chinesechecker/game/Bot;", "rnd", "Lkotlin/random/Random;", "(Lkotlin/random/Random;)V", "chooseMove", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "player", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "app_debug"})
public final class WeakBot implements com.yoyicue.chinesechecker.game.Bot {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.random.Random rnd = null;
    
    public WeakBot(@org.jetbrains.annotations.NotNull()
    kotlin.random.Random rnd) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.yoyicue.chinesechecker.game.Board.Move chooseMove(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return null;
    }
    
    public WeakBot() {
        super();
    }
}