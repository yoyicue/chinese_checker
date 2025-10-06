package com.yoyicue.chinesechecker.game;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\bH\u0002\u00a8\u0006\u000e"}, d2 = {"Lcom/yoyicue/chinesechecker/game/GreedyBot;", "Lcom/yoyicue/chinesechecker/game/Bot;", "()V", "chooseMove", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "player", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "evalAfter", "", "b", "move", "me", "app_debug"})
public final class GreedyBot implements com.yoyicue.chinesechecker.game.Bot {
    
    public GreedyBot() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.yoyicue.chinesechecker.game.Board.Move chooseMove(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return null;
    }
    
    private final int evalAfter(com.yoyicue.chinesechecker.game.Board b, com.yoyicue.chinesechecker.game.Board.Move move, com.yoyicue.chinesechecker.game.Board.PlayerId me) {
        return 0;
    }
}