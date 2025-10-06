package com.yoyicue.chinesechecker.game;

/**
 * Minimax with alpha-beta pruning and simple move ordering.
 * - Iterative deepening within a small time budget.
 * - For games with >2 players, uses a Paranoid approximation (others minimize my score).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0016\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001:\u0002,-B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\u00d7\u0001\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00032\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00050\u00122\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0006\u0010\u0017\u001a\u00020\u00162\u0012\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u001a0\u00192\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001d0\u001c2\u000e\u0010\u001e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u00152\u000e\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001f0\u00152\u001e\u0010!\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050#\u0012\u0004\u0012\u00020\u00050\"2\u0006\u0010$\u001a\u00020\u0005H\u0002\u00a2\u0006\u0002\u0010%J\u001a\u0010&\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\'\u001a\u00020\fH\u0016J2\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0005\u0018\u00010#2\u0006\u0010)\u001a\u00020\u001f2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00050\u0012H\u0002J\u0018\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/yoyicue/chinesechecker/game/SmartBot;", "Lcom/yoyicue/chinesechecker/game/Bot;", "timeBudgetMs", "", "maxDepth", "", "ttCapacity", "(JII)V", "alphaBeta", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "me", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "depth", "alphaInit", "betaInit", "deadlineNs", "indexByHex", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "zob", "", "", "sideZ", "tt", "Ljava/util/LinkedHashMap;", "Lcom/yoyicue/chinesechecker/game/SmartBot$TTEntry;", "ttPut", "Lkotlin/Function1;", "", "killers1", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "killers2", "history", "", "Lkotlin/Pair;", "ply", "(Lcom/yoyicue/chinesechecker/game/Board;Lcom/yoyicue/chinesechecker/game/Board$PlayerId;IIIJLjava/util/Map;[[J[JLjava/util/LinkedHashMap;Lkotlin/jvm/functions/Function1;[Lcom/yoyicue/chinesechecker/game/Board$Move;[Lcom/yoyicue/chinesechecker/game/Board$Move;Ljava/util/Map;I)I", "chooseMove", "player", "fromToKey", "m", "score", "b", "Bound", "TTEntry", "app_debug"})
public final class SmartBot implements com.yoyicue.chinesechecker.game.Bot {
    private final long timeBudgetMs = 0L;
    private final int maxDepth = 0;
    private final int ttCapacity = 0;
    
    public SmartBot(long timeBudgetMs, int maxDepth, int ttCapacity) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.yoyicue.chinesechecker.game.Board.Move chooseMove(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return null;
    }
    
    private final int alphaBeta(com.yoyicue.chinesechecker.game.Board board, com.yoyicue.chinesechecker.game.Board.PlayerId me, int depth, int alphaInit, int betaInit, long deadlineNs, java.util.Map<com.yoyicue.chinesechecker.game.Hex, java.lang.Integer> indexByHex, long[][] zob, long[] sideZ, java.util.LinkedHashMap<java.lang.Long, com.yoyicue.chinesechecker.game.SmartBot.TTEntry> tt, kotlin.jvm.functions.Function1<? super com.yoyicue.chinesechecker.game.SmartBot.TTEntry, kotlin.Unit> ttPut, com.yoyicue.chinesechecker.game.Board.Move[] killers1, com.yoyicue.chinesechecker.game.Board.Move[] killers2, java.util.Map<kotlin.Pair<java.lang.Integer, java.lang.Integer>, java.lang.Integer> history, int ply) {
        return 0;
    }
    
    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> fromToKey(com.yoyicue.chinesechecker.game.Board.Move m, java.util.Map<com.yoyicue.chinesechecker.game.Hex, java.lang.Integer> indexByHex) {
        return null;
    }
    
    private final int score(com.yoyicue.chinesechecker.game.Board b, com.yoyicue.chinesechecker.game.Board.PlayerId me) {
        return 0;
    }
    
    public SmartBot() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/yoyicue/chinesechecker/game/SmartBot$Bound;", "", "(Ljava/lang/String;I)V", "EXACT", "LOWER", "UPPER", "app_debug"})
    static enum Bound {
        /*public static final*/ EXACT /* = new EXACT() */,
        /*public static final*/ LOWER /* = new LOWER() */,
        /*public static final*/ UPPER /* = new UPPER() */;
        
        Bound() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.yoyicue.chinesechecker.game.SmartBot.Bound> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\bH\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\nH\u00c6\u0003J=\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001R\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011\u00a8\u0006!"}, d2 = {"Lcom/yoyicue/chinesechecker/game/SmartBot$TTEntry;", "", "key", "", "depth", "", "score", "bound", "Lcom/yoyicue/chinesechecker/game/SmartBot$Bound;", "best", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "(JIILcom/yoyicue/chinesechecker/game/SmartBot$Bound;Lcom/yoyicue/chinesechecker/game/Board$Move;)V", "getBest", "()Lcom/yoyicue/chinesechecker/game/Board$Move;", "getBound", "()Lcom/yoyicue/chinesechecker/game/SmartBot$Bound;", "getDepth", "()I", "getKey", "()J", "getScore", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
    static final class TTEntry {
        private final long key = 0L;
        private final int depth = 0;
        private final int score = 0;
        @org.jetbrains.annotations.NotNull()
        private final com.yoyicue.chinesechecker.game.SmartBot.Bound bound = null;
        @org.jetbrains.annotations.Nullable()
        private final com.yoyicue.chinesechecker.game.Board.Move best = null;
        
        public TTEntry(long key, int depth, int score, @org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.game.SmartBot.Bound bound, @org.jetbrains.annotations.Nullable()
        com.yoyicue.chinesechecker.game.Board.Move best) {
            super();
        }
        
        public final long getKey() {
            return 0L;
        }
        
        public final int getDepth() {
            return 0;
        }
        
        public final int getScore() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.SmartBot.Bound getBound() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.yoyicue.chinesechecker.game.Board.Move getBest() {
            return null;
        }
        
        public final long component1() {
            return 0L;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.SmartBot.Bound component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.yoyicue.chinesechecker.game.Board.Move component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.SmartBot.TTEntry copy(long key, int depth, int score, @org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.game.SmartBot.Bound bound, @org.jetbrains.annotations.Nullable()
        com.yoyicue.chinesechecker.game.Board.Move best) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}