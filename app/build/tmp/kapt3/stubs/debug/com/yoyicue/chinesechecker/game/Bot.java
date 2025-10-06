package com.yoyicue.chinesechecker.game;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a8\u0006\b"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Bot;", "", "chooseMove", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "player", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "app_debug"})
public abstract interface Bot {
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.yoyicue.chinesechecker.game.Board.Move chooseMove(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player);
}