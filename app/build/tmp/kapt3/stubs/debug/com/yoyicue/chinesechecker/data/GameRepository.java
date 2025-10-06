package com.yoyicue.chinesechecker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 \'2\u00020\u0001:\u0006\'()*+,B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u001d\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0014\u0010\u0015J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0017H\u0086@\u00a2\u0006\u0002\u0010\u000fJ8\u0010\u001d\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u001f2\u000e\u0010 \u001a\n\u0012\u0004\u0012\u00020\"\u0018\u00010!2\b\u0010#\u001a\u0004\u0018\u00010\u00132\u0006\u0010$\u001a\u00020%H\u0086@\u00a2\u0006\u0002\u0010&R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\n8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006-"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository;", "", "context", "Landroid/content/Context;", "dao", "Lcom/yoyicue/chinesechecker/data/db/SaveGameDao;", "(Landroid/content/Context;Lcom/yoyicue/chinesechecker/data/db/SaveGameDao;)V", "json", "Lkotlinx/serialization/json/Json;", "legacySaveFile", "Ljava/io/File;", "getLegacySaveFile", "()Ljava/io/File;", "clearSave", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "defaultColorFor", "Landroidx/compose/ui/graphics/Color;", "pid", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "defaultColorFor-vNxB06k", "(Lcom/yoyicue/chinesechecker/game/Board$PlayerId;)J", "fromEntity", "Lcom/yoyicue/chinesechecker/data/GameRepository$Restored;", "row", "Lcom/yoyicue/chinesechecker/data/db/SaveGameEntity;", "hasSave", "", "load", "save", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "lastMovePath", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "lastMoveOwner", "config", "Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "(Lcom/yoyicue/chinesechecker/game/Board;Ljava/util/List;Lcom/yoyicue/chinesechecker/game/Board$PlayerId;Lcom/yoyicue/chinesechecker/ui/game/GameConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "GameSave", "HexDTO", "OccupantDTO", "PlayerCfgDTO", "Restored", "app_debug"})
public final class GameRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.db.SaveGameDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "GameRepository";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.yoyicue.chinesechecker.data.GameRepository.Companion Companion = null;
    
    public GameRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.data.db.SaveGameDao dao) {
        super();
    }
    
    private final java.io.File getLegacySaveFile() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object save(@org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.Nullable()
    java.util.List<com.yoyicue.chinesechecker.game.Hex> lastMovePath, @org.jetbrains.annotations.Nullable()
    com.yoyicue.chinesechecker.game.Board.PlayerId lastMoveOwner, @org.jetbrains.annotations.NotNull()
    com.yoyicue.chinesechecker.ui.game.GameConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object clearSave(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object load(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.yoyicue.chinesechecker.data.GameRepository.Restored> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object hasSave(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final com.yoyicue.chinesechecker.data.GameRepository.Restored fromEntity(com.yoyicue.chinesechecker.data.db.SaveGameEntity row) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlinx.serialization.Serializable()
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0083\b\u0018\u0000 12\u00020\u0001:\u000201Be\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\b\u0012\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0006\u0012\u000e\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u000e\u0018\u00010\b\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u00a2\u0006\u0002\u0010\u0011BK\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u0012\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0006\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b\u00a2\u0006\u0002\u0010\u0012J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00c6\u0003J\u0011\u0010\u001f\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\bH\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000e0\bH\u00c6\u0003J[\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0010\b\u0002\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00062\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\bH\u00c6\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\'\u001a\u00020\u0006H\u00d6\u0001J&\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020\u00002\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u00c1\u0001\u00a2\u0006\u0002\b/R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0019\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016\u00a8\u00062"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$GameSave;", "", "seen1", "", "playerCount", "currentPlayer", "", "occupant", "", "Lcom/yoyicue/chinesechecker/data/GameRepository$OccupantDTO;", "lastMove", "Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "lastOwner", "players", "Lcom/yoyicue/chinesechecker/data/GameRepository$PlayerCfgDTO;", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(IILjava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/util/List;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(ILjava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/util/List;)V", "getCurrentPlayer", "()Ljava/lang/String;", "getLastMove", "()Ljava/util/List;", "getLastOwner", "getOccupant", "getPlayerCount", "()I", "getPlayers", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "$serializer", "Companion", "app_debug"})
    static final class GameSave {
        private final int playerCount = 0;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String currentPlayer = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> occupant = null;
        @org.jetbrains.annotations.Nullable()
        private final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> lastMove = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String lastOwner = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> players = null;
        @org.jetbrains.annotations.NotNull()
        public static final com.yoyicue.chinesechecker.data.GameRepository.GameSave.Companion Companion = null;
        
        public GameSave(int playerCount, @org.jetbrains.annotations.NotNull()
        java.lang.String currentPlayer, @org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> occupant, @org.jetbrains.annotations.Nullable()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> lastMove, @org.jetbrains.annotations.Nullable()
        java.lang.String lastOwner, @org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> players) {
            super();
        }
        
        public final int getPlayerCount() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCurrentPlayer() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> getOccupant() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> getLastMove() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getLastOwner() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> getPlayers() {
            return null;
        }
        
        public final int component1() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.GameSave copy(int playerCount, @org.jetbrains.annotations.NotNull()
        java.lang.String currentPlayer, @org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> occupant, @org.jetbrains.annotations.Nullable()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> lastMove, @org.jetbrains.annotations.Nullable()
        java.lang.String lastOwner, @org.jetbrains.annotations.NotNull()
        java.util.List<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> players) {
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
        
        @kotlin.jvm.JvmStatic()
        public static final void write$Self$app_debug(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.GameSave self, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.encoding.CompositeEncoder output, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.descriptors.SerialDescriptor serialDesc) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tH\u00d6\u0001\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0002H\u00d6\u0001R\u0014\u0010\u0004\u001a\u00020\u00058VX\u00d6\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0014"}, d2 = {"com/yoyicue/chinesechecker/data/GameRepository.GameSave.$serializer", "Lkotlinx/serialization/internal/GeneratedSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$GameSave;", "()V", "descriptor", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "getDescriptor", "()Lkotlinx/serialization/descriptors/SerialDescriptor;", "childSerializers", "", "Lkotlinx/serialization/KSerializer;", "()[Lkotlinx/serialization/KSerializer;", "deserialize", "decoder", "Lkotlinx/serialization/encoding/Decoder;", "serialize", "", "encoder", "Lkotlinx/serialization/encoding/Encoder;", "value", "app_debug"})
        @java.lang.Deprecated()
        public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.yoyicue.chinesechecker.data.GameRepository.GameSave> {
            @org.jetbrains.annotations.NotNull()
            public static final com.yoyicue.chinesechecker.data.GameRepository.GameSave.$serializer INSTANCE = null;
            
            private $serializer() {
                super();
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] childSerializers() {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public com.yoyicue.chinesechecker.data.GameRepository.GameSave deserialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Decoder decoder) {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
                return null;
            }
            
            @java.lang.Override()
            public void serialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull()
            com.yoyicue.chinesechecker.data.GameRepository.GameSave value) {
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] typeParametersSerializers() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00c6\u0001\u00a8\u0006\u0006"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$GameSave$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$GameSave;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final kotlinx.serialization.KSerializer<com.yoyicue.chinesechecker.data.GameRepository.GameSave> serializer() {
                return null;
            }
        }
    }
    
    @kotlinx.serialization.Serializable()
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0083\b\u0018\u0000 \"2\u00020\u0001:\u0002!\"B1\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tB\u001d\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\nJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\'\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J&\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u00c1\u0001\u00a2\u0006\u0002\b R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006#"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "", "seen1", "", "x", "y", "z", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(IIIILkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(III)V", "getX", "()I", "getY", "getZ", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "$serializer", "Companion", "app_debug"})
    static final class HexDTO {
        private final int x = 0;
        private final int y = 0;
        private final int z = 0;
        @org.jetbrains.annotations.NotNull()
        public static final com.yoyicue.chinesechecker.data.GameRepository.HexDTO.Companion Companion = null;
        
        public HexDTO(int x, int y, int z) {
            super();
        }
        
        public final int getX() {
            return 0;
        }
        
        public final int getY() {
            return 0;
        }
        
        public final int getZ() {
            return 0;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.HexDTO copy(int x, int y, int z) {
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
        
        @kotlin.jvm.JvmStatic()
        public static final void write$Self$app_debug(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.HexDTO self, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.encoding.CompositeEncoder output, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.descriptors.SerialDescriptor serialDesc) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tH\u00d6\u0001\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0002H\u00d6\u0001R\u0014\u0010\u0004\u001a\u00020\u00058VX\u00d6\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0014"}, d2 = {"com/yoyicue/chinesechecker/data/GameRepository.HexDTO.$serializer", "Lkotlinx/serialization/internal/GeneratedSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "()V", "descriptor", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "getDescriptor", "()Lkotlinx/serialization/descriptors/SerialDescriptor;", "childSerializers", "", "Lkotlinx/serialization/KSerializer;", "()[Lkotlinx/serialization/KSerializer;", "deserialize", "decoder", "Lkotlinx/serialization/encoding/Decoder;", "serialize", "", "encoder", "Lkotlinx/serialization/encoding/Encoder;", "value", "app_debug"})
        @java.lang.Deprecated()
        public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> {
            @org.jetbrains.annotations.NotNull()
            public static final com.yoyicue.chinesechecker.data.GameRepository.HexDTO.$serializer INSTANCE = null;
            
            private $serializer() {
                super();
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] childSerializers() {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public com.yoyicue.chinesechecker.data.GameRepository.HexDTO deserialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Decoder decoder) {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
                return null;
            }
            
            @java.lang.Override()
            public void serialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull()
            com.yoyicue.chinesechecker.data.GameRepository.HexDTO value) {
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] typeParametersSerializers() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00c6\u0001\u00a8\u0006\u0006"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final kotlinx.serialization.KSerializer<com.yoyicue.chinesechecker.data.GameRepository.HexDTO> serializer() {
                return null;
            }
        }
    }
    
    @kotlinx.serialization.Serializable()
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0083\b\u0018\u0000 !2\u00020\u0001:\u0002 !B-\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\nB\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0010\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0007H\u00c6\u0003J\u001d\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0007H\u00d6\u0001J&\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u00c1\u0001\u00a2\u0006\u0002\b\u001fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\""}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$OccupantDTO;", "", "seen1", "", "hex", "Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "player", "", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;Ljava/lang/String;Lkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;Ljava/lang/String;)V", "getHex", "()Lcom/yoyicue/chinesechecker/data/GameRepository$HexDTO;", "getPlayer", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "$serializer", "Companion", "app_debug"})
    static final class OccupantDTO {
        @org.jetbrains.annotations.NotNull()
        private final com.yoyicue.chinesechecker.data.GameRepository.HexDTO hex = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String player = null;
        @org.jetbrains.annotations.NotNull()
        public static final com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO.Companion Companion = null;
        
        public OccupantDTO(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.HexDTO hex, @org.jetbrains.annotations.NotNull()
        java.lang.String player) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.HexDTO getHex() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getPlayer() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.HexDTO component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO copy(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.HexDTO hex, @org.jetbrains.annotations.NotNull()
        java.lang.String player) {
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
        
        @kotlin.jvm.JvmStatic()
        public static final void write$Self$app_debug(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO self, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.encoding.CompositeEncoder output, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.descriptors.SerialDescriptor serialDesc) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tH\u00d6\u0001\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0002H\u00d6\u0001R\u0014\u0010\u0004\u001a\u00020\u00058VX\u00d6\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0014"}, d2 = {"com/yoyicue/chinesechecker/data/GameRepository.OccupantDTO.$serializer", "Lkotlinx/serialization/internal/GeneratedSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$OccupantDTO;", "()V", "descriptor", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "getDescriptor", "()Lkotlinx/serialization/descriptors/SerialDescriptor;", "childSerializers", "", "Lkotlinx/serialization/KSerializer;", "()[Lkotlinx/serialization/KSerializer;", "deserialize", "decoder", "Lkotlinx/serialization/encoding/Decoder;", "serialize", "", "encoder", "Lkotlinx/serialization/encoding/Encoder;", "value", "app_debug"})
        @java.lang.Deprecated()
        public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> {
            @org.jetbrains.annotations.NotNull()
            public static final com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO.$serializer INSTANCE = null;
            
            private $serializer() {
                super();
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] childSerializers() {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO deserialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Decoder decoder) {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
                return null;
            }
            
            @java.lang.Override()
            public void serialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull()
            com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO value) {
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] typeParametersSerializers() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00c6\u0001\u00a8\u0006\u0006"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$OccupantDTO$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$OccupantDTO;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final kotlinx.serialization.KSerializer<com.yoyicue.chinesechecker.data.GameRepository.OccupantDTO> serializer() {
                return null;
            }
        }
    }
    
    @kotlinx.serialization.Serializable()
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0083\b\u0018\u0000 \'2\u00020\u0001:\u0002&\'B?\b\u0011\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\fB\'\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\tH\u00c6\u0003J3\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0005H\u00d6\u0001J&\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00002\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u00c1\u0001\u00a2\u0006\u0002\b%R\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011\u00a8\u0006("}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$PlayerCfgDTO;", "", "seen1", "", "id", "", "controller", "difficulty", "color", "", "serializationConstructorMarker", "Lkotlinx/serialization/internal/SerializationConstructorMarker;", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;JLkotlinx/serialization/internal/SerializationConstructorMarker;)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;J)V", "getColor", "()J", "getController", "()Ljava/lang/String;", "getDifficulty", "getId", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "write$Self", "", "self", "output", "Lkotlinx/serialization/encoding/CompositeEncoder;", "serialDesc", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "write$Self$app_debug", "$serializer", "Companion", "app_debug"})
    static final class PlayerCfgDTO {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String id = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String controller = null;
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String difficulty = null;
        private final long color = 0L;
        @org.jetbrains.annotations.NotNull()
        public static final com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO.Companion Companion = null;
        
        public PlayerCfgDTO(@org.jetbrains.annotations.NotNull()
        java.lang.String id, @org.jetbrains.annotations.NotNull()
        java.lang.String controller, @org.jetbrains.annotations.Nullable()
        java.lang.String difficulty, long color) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getController() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getDifficulty() {
            return null;
        }
        
        public final long getColor() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }
        
        public final long component4() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO copy(@org.jetbrains.annotations.NotNull()
        java.lang.String id, @org.jetbrains.annotations.NotNull()
        java.lang.String controller, @org.jetbrains.annotations.Nullable()
        java.lang.String difficulty, long color) {
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
        
        @kotlin.jvm.JvmStatic()
        public static final void write$Self$app_debug(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO self, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.encoding.CompositeEncoder output, @org.jetbrains.annotations.NotNull()
        kotlinx.serialization.descriptors.SerialDescriptor serialDesc) {
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0018\u0010\b\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\tH\u00d6\u0001\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\u0019\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0002H\u00d6\u0001R\u0014\u0010\u0004\u001a\u00020\u00058VX\u00d6\u0005\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0014"}, d2 = {"com/yoyicue/chinesechecker/data/GameRepository.PlayerCfgDTO.$serializer", "Lkotlinx/serialization/internal/GeneratedSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$PlayerCfgDTO;", "()V", "descriptor", "Lkotlinx/serialization/descriptors/SerialDescriptor;", "getDescriptor", "()Lkotlinx/serialization/descriptors/SerialDescriptor;", "childSerializers", "", "Lkotlinx/serialization/KSerializer;", "()[Lkotlinx/serialization/KSerializer;", "deserialize", "decoder", "Lkotlinx/serialization/encoding/Decoder;", "serialize", "", "encoder", "Lkotlinx/serialization/encoding/Encoder;", "value", "app_debug"})
        @java.lang.Deprecated()
        public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> {
            @org.jetbrains.annotations.NotNull()
            public static final com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO.$serializer INSTANCE = null;
            
            private $serializer() {
                super();
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] childSerializers() {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO deserialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Decoder decoder) {
                return null;
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
                return null;
            }
            
            @java.lang.Override()
            public void serialize(@org.jetbrains.annotations.NotNull()
            kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull()
            com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO value) {
            }
            
            @java.lang.Override()
            @org.jetbrains.annotations.NotNull()
            public kotlinx.serialization.KSerializer<?>[] typeParametersSerializers() {
                return null;
            }
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00c6\u0001\u00a8\u0006\u0006"}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$PlayerCfgDTO$Companion;", "", "()V", "serializer", "Lkotlinx/serialization/KSerializer;", "Lcom/yoyicue/chinesechecker/data/GameRepository$PlayerCfgDTO;", "app_debug"})
        public static final class Companion {
            
            private Companion() {
                super();
            }
            
            @org.jetbrains.annotations.NotNull()
            public final kotlinx.serialization.KSerializer<com.yoyicue.chinesechecker.data.GameRepository.PlayerCfgDTO> serializer() {
                return null;
            }
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\u0011\u0010\u0015\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005H\u00c6\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010\u0017\u001a\u00020\nH\u00c6\u0003J;\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0010\b\u0002\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u001dH\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0019\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006 "}, d2 = {"Lcom/yoyicue/chinesechecker/data/GameRepository$Restored;", "", "board", "Lcom/yoyicue/chinesechecker/game/Board;", "lastMovePath", "", "Lcom/yoyicue/chinesechecker/game/Hex;", "lastMoveOwner", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "config", "Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "(Lcom/yoyicue/chinesechecker/game/Board;Ljava/util/List;Lcom/yoyicue/chinesechecker/game/Board$PlayerId;Lcom/yoyicue/chinesechecker/ui/game/GameConfig;)V", "getBoard", "()Lcom/yoyicue/chinesechecker/game/Board;", "getConfig", "()Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "getLastMoveOwner", "()Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "getLastMovePath", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
    public static final class Restored {
        @org.jetbrains.annotations.NotNull()
        private final com.yoyicue.chinesechecker.game.Board board = null;
        @org.jetbrains.annotations.Nullable()
        private final java.util.List<com.yoyicue.chinesechecker.game.Hex> lastMovePath = null;
        @org.jetbrains.annotations.Nullable()
        private final com.yoyicue.chinesechecker.game.Board.PlayerId lastMoveOwner = null;
        @org.jetbrains.annotations.NotNull()
        private final com.yoyicue.chinesechecker.ui.game.GameConfig config = null;
        
        public Restored(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.Nullable()
        java.util.List<com.yoyicue.chinesechecker.game.Hex> lastMovePath, @org.jetbrains.annotations.Nullable()
        com.yoyicue.chinesechecker.game.Board.PlayerId lastMoveOwner, @org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.ui.game.GameConfig config) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board getBoard() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.yoyicue.chinesechecker.game.Hex> getLastMovePath() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.yoyicue.chinesechecker.game.Board.PlayerId getLastMoveOwner() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.ui.game.GameConfig getConfig() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.game.Board component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.util.List<com.yoyicue.chinesechecker.game.Hex> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.yoyicue.chinesechecker.game.Board.PlayerId component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.ui.game.GameConfig component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.yoyicue.chinesechecker.data.GameRepository.Restored copy(@org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.game.Board board, @org.jetbrains.annotations.Nullable()
        java.util.List<com.yoyicue.chinesechecker.game.Hex> lastMovePath, @org.jetbrains.annotations.Nullable()
        com.yoyicue.chinesechecker.game.Board.PlayerId lastMoveOwner, @org.jetbrains.annotations.NotNull()
        com.yoyicue.chinesechecker.ui.game.GameConfig config) {
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