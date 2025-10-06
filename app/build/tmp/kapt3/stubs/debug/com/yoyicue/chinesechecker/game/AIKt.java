package com.yoyicue.chinesechecker.game;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0018\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\f\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\r\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\u000f\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"CENTER_HEX", "Lcom/yoyicue/chinesechecker/game/Hex;", "ENTERED_COUNT_WEIGHT", "", "QUIESCENCE_JUMP_CAP", "ROOT_ORDER_SCORE_SCALE", "", "distanceSumToGoal", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "player", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "effectiveDistance", "enteredCount", "homePenalty", "progressMeasure", "app_debug"})
public final class AIKt {
    @org.jetbrains.annotations.NotNull()
    private static final com.yoyicue.chinesechecker.game.Hex CENTER_HEX = null;
    private static final int ENTERED_COUNT_WEIGHT = 120;
    private static final int QUIESCENCE_JUMP_CAP = 12;
    private static final long ROOT_ORDER_SCORE_SCALE = 1000L;
    
    private static final int distanceSumToGoal(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return 0;
    }
    
    private static final int homePenalty(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return 0;
    }
    
    private static final int effectiveDistance(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return 0;
    }
    
    private static final int progressMeasure(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return 0;
    }
    
    private static final int enteredCount(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return 0;
    }
}