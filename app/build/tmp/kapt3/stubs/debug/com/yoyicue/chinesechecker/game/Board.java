package com.yoyicue.chinesechecker.game;

/**
 * Chinese Checkers board with 121 positions: hex radius 4 core + 6 arms of length 4.
 * Coordinates: cube hex (x+y+z=0). A position is valid if max(|x|,|y|,|z|) <= 8 and min(|x|,|y|,|z|) <= 4.
 *
 * Supports 2/3/4/6 players by mapping players to the six camps (triangles) and
 * defining each player's goal as the opposite camp.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u0000 12\u00020\u0001:\u00040123By\b\u0002\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u0012\u0018\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u000b\u0012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u000b\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\t2\u0006\u0010\u001f\u001a\u00020\u0004H\u0002J\u0014\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00040\t2\u0006\u0010!\u001a\u00020\u0007J\u000e\u0010\"\u001a\u00020\u00002\u0006\u0010#\u001a\u00020\u001eJ\u0010\u0010$\u001a\u0004\u0018\u00010\u00072\u0006\u0010%\u001a\u00020\u0004J\u0006\u0010&\u001a\u00020\u0000J\u0014\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010(\u001a\u00020\u0007J\u000e\u0010)\u001a\u00020*2\u0006\u0010%\u001a\u00020\u0004J\u0014\u0010+\u001a\b\u0012\u0004\u0012\u00020\u001e0\t2\u0006\u0010!\u001a\u00020\u0007J\u0014\u0010,\u001a\b\u0012\u0004\u0012\u00020\u001e0\t2\u0006\u0010%\u001a\u00020\u0004J\u0006\u0010-\u001a\u00020\u0000J\u0014\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010(\u001a\u00020\u0007J\b\u0010/\u001a\u0004\u0018\u00010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R#\u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\u00030\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0015\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0007@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001d\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0013R\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001d\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013\u00a8\u00064"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board;", "", "positions", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "occupant", "", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "activePlayers", "", "campCells", "", "Lcom/yoyicue/chinesechecker/game/Board$Camp;", "startCampOf", "goalCampOf", "(Ljava/util/Set;Ljava/util/Map;Ljava/util/List;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;)V", "getActivePlayers", "()Ljava/util/List;", "getCampCells", "()Ljava/util/Map;", "<set-?>", "currentPlayer", "getCurrentPlayer", "()Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "getGoalCampOf", "getOccupant", "getPositions", "()Ljava/util/Set;", "getStartCampOf", "allJumpsFrom", "Lcom/yoyicue/chinesechecker/game/Board$Move;", "start", "allPieces", "p", "apply", "move", "at", "h", "copy", "goalCampCells", "player", "isEmpty", "", "legalMovesFor", "legalMovesFrom", "pass", "startCampCells", "winner", "Camp", "Companion", "Move", "PlayerId", "app_debug"})
public final class Board {
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<com.yoyicue.chinesechecker.game.Hex> positions = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.yoyicue.chinesechecker.game.Hex, com.yoyicue.chinesechecker.game.Board.PlayerId> occupant = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.yoyicue.chinesechecker.game.Board.PlayerId> activePlayers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.yoyicue.chinesechecker.game.Board.Camp, java.util.Set<com.yoyicue.chinesechecker.game.Hex>> campCells = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> startCampOf = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> goalCampOf = null;
    @org.jetbrains.annotations.NotNull()
    private com.yoyicue.chinesechecker.game.Board.PlayerId currentPlayer = com.yoyicue.chinesechecker.game.Board.PlayerId.A;
    @kotlin.jvm.Volatile()
    private static volatile boolean ALLOW_LONG_JUMPS = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.yoyicue.chinesechecker.game.Board.Companion Companion = null;
    
    private Board(java.util.Set<com.yoyicue.chinesechecker.game.Hex> positions, java.util.Map<com.yoyicue.chinesechecker.game.Hex, com.yoyicue.chinesechecker.game.Board.PlayerId> occupant, java.util.List<? extends com.yoyicue.chinesechecker.game.Board.PlayerId> activePlayers, java.util.Map<com.yoyicue.chinesechecker.game.Board.Camp, ? extends java.util.Set<com.yoyicue.chinesechecker.game.Hex>> campCells, java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, ? extends com.yoyicue.chinesechecker.game.Board.Camp> startCampOf, java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, ? extends com.yoyicue.chinesechecker.game.Board.Camp> goalCampOf) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.yoyicue.chinesechecker.game.Hex> getPositions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.yoyicue.chinesechecker.game.Hex, com.yoyicue.chinesechecker.game.Board.PlayerId> getOccupant() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.yoyicue.chinesechecker.game.Board.PlayerId> getActivePlayers() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.yoyicue.chinesechecker.game.Board.Camp, java.util.Set<com.yoyicue.chinesechecker.game.Hex>> getCampCells() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> getStartCampOf() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> getGoalCampOf() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.game.Board copy() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.yoyicue.chinesechecker.game.Board.PlayerId at(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Hex h) {
        return null;
    }
    
    public final boolean isEmpty(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Hex h) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.yoyicue.chinesechecker.game.Hex> allPieces(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId p) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.yoyicue.chinesechecker.game.Board.Move> legalMovesFrom(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Hex h) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.yoyicue.chinesechecker.game.Board.Move> legalMovesFor(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId p) {
        return null;
    }
    
    private final java.util.List<com.yoyicue.chinesechecker.game.Board.Move> allJumpsFrom(com.yoyicue.chinesechecker.game.Hex start) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.game.Board apply(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.Move move) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.game.Board pass() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.yoyicue.chinesechecker.game.Board.PlayerId winner() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.game.Board.PlayerId getCurrentPlayer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.yoyicue.chinesechecker.game.Hex> goalCampCells(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<com.yoyicue.chinesechecker.game.Hex> startCampCells(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board.PlayerId player) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board$Camp;", "", "(Ljava/lang/String;I)V", "Top", "Bottom", "NE", "SW", "NW", "SE", "app_debug"})
    public static enum Camp {
        /*public static final*/ Top /* = new Top() */,
        /*public static final*/ Bottom /* = new Bottom() */,
        /*public static final*/ NE /* = new NE() */,
        /*public static final*/ SW /* = new SW() */,
        /*public static final*/ NW /* = new NW() */,
        /*public static final*/ SE /* = new SE() */;
        
        Camp() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.yoyicue.chinesechecker.game.Board.Camp> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u001bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0002J(\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\nH\u0002J*\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000f2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00150\t2\u0006\u0010\u0016\u001a\u00020\u0015J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0004J\u0006\u0010\u001a\u001a\u00020\u0013J\u000e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board$Companion;", "", "()V", "ALLOW_LONG_JUMPS", "", "buildPositions", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "computeCampCells", "", "Lcom/yoyicue/chinesechecker/game/Board$Camp;", "positions", "defaultAssignment", "Lcom/yoyicue/chinesechecker/game/Board$Companion$Assignment;", "playerCount", "", "oppositeOf", "camp", "restore", "Lcom/yoyicue/chinesechecker/game/Board;", "occupant", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "currentPlayer", "setLongJumps", "", "enabled", "standard", "Assignment", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void setLongJumps(boolean enabled) {
        }
        
        private final java.util.Set<com.yoyicue.chinesechecker.game.Hex> buildPositions() {
            return null;
        }
        
        private final java.util.Map<com.yoyicue.chinesechecker.game.Board.Camp, java.util.Set<com.yoyicue.chinesechecker.game.Hex>> computeCampCells(java.util.Set<com.yoyicue.chinesechecker.game.Hex> positions) {
            return null;
        }
        
        private final com.yoyicue.chinesechecker.game.Board.Camp oppositeOf(com.yoyicue.chinesechecker.game.Board.Camp camp) {
            return null;
        }
        
        private final com.yoyicue.chinesechecker.game.Board.Companion.Assignment defaultAssignment(int playerCount) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board standard() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board standard(int playerCount) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board restore(int playerCount, @org.jetbrains.annotations.NotNull()
        java.util.Map<com.yoyicue.chinesechecker.game.Hex, ? extends com.yoyicue.chinesechecker.game.Board.PlayerId> occupant, @org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.game.Board.PlayerId currentPlayer) {
            return null;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\'\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\bJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0015\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J/\u0010\u000f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0014\b\u0002\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0017"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board$Companion$Assignment;", "", "order", "", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "start", "", "Lcom/yoyicue/chinesechecker/game/Board$Camp;", "(Ljava/util/List;Ljava/util/Map;)V", "getOrder", "()Ljava/util/List;", "getStart", "()Ljava/util/Map;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
        static final class Assignment {
            @org.jetbrains.annotations.NotNull()
            private final java.util.List<com.yoyicue.chinesechecker.game.Board.PlayerId> order = null;
            @org.jetbrains.annotations.NotNull()
            private final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> start = null;
            
            public Assignment(@org.jetbrains.annotations.NotNull()
            java.util.List<? extends com.yoyicue.chinesechecker.game.Board.PlayerId> order, @org.jetbrains.annotations.NotNull()
            java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, ? extends com.yoyicue.chinesechecker.game.Board.Camp> start) {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.util.List<com.yoyicue.chinesechecker.game.Board.PlayerId> getOrder() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> getStart() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.util.List<com.yoyicue.chinesechecker.game.Board.PlayerId> component1() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, com.yoyicue.chinesechecker.game.Board.Camp> component2() {
                return null;
            }
            
            @org.jetbrains.annotations.NotNull()
            public final com.yoyicue.chinesechecker.game.Board.Companion.Assignment copy(@org.jetbrains.annotations.NotNull()
            java.util.List<? extends com.yoyicue.chinesechecker.game.Board.PlayerId> order, @org.jetbrains.annotations.NotNull()
            java.util.Map<com.yoyicue.chinesechecker.game.Board.PlayerId, ? extends com.yoyicue.chinesechecker.game.Board.Camp> start) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0019\u0010\u0011\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\n2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0017H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\b\u00a8\u0006\u0018"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board$Move;", "", "path", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "(Ljava/util/List;)V", "from", "getFrom", "()Lcom/yoyicue/chinesechecker/game/Hex;", "isJump", "", "()Z", "getPath", "()Ljava/util/List;", "to", "getTo", "component1", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class Move {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.yoyicue.chinesechecker.game.Hex> path = null;
        
        public Move(@org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.game.Hex> path) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.game.Hex> getPath() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Hex getFrom() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Hex getTo() {
            return null;
        }
        
        public final boolean isJump() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.game.Hex> component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board.Move copy(@org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.game.Hex> path) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b\u00a8\u0006\t"}, d2 = {"Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "", "(Ljava/lang/String;I)V", "A", "B", "C", "D", "E", "F", "app_debug"})
    public static enum PlayerId {
        /*public static final*/ A /* = new A() */,
        /*public static final*/ B /* = new B() */,
        /*public static final*/ C /* = new C() */,
        /*public static final*/ D /* = new D() */,
        /*public static final*/ E /* = new E() */,
        /*public static final*/ F /* = new F() */;
        
        PlayerId() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.yoyicue.chinesechecker.game.Board.PlayerId> getEntries() {
            return null;
        }
    }
}