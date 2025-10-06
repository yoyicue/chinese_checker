package com.yoyicue.chinesechecker.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u001b\u001a\u00020\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u001f\u001a\u00020 \u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\"\u00a8\u0006#"}, d2 = {"Lcom/yoyicue/chinesechecker/data/AppContainer;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "db", "Lcom/yoyicue/chinesechecker/data/db/AppDatabase;", "gameRepository", "Lcom/yoyicue/chinesechecker/data/GameRepository;", "getGameRepository", "()Lcom/yoyicue/chinesechecker/data/GameRepository;", "lastGameConfig", "Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "getLastGameConfig", "()Lcom/yoyicue/chinesechecker/ui/game/GameConfig;", "setLastGameConfig", "(Lcom/yoyicue/chinesechecker/ui/game/GameConfig;)V", "pendingRestore", "Lcom/yoyicue/chinesechecker/data/GameRepository$Restored;", "getPendingRestore", "()Lcom/yoyicue/chinesechecker/data/GameRepository$Restored;", "setPendingRestore", "(Lcom/yoyicue/chinesechecker/data/GameRepository$Restored;)V", "profileRepository", "Lcom/yoyicue/chinesechecker/data/ProfileRepository;", "getProfileRepository", "()Lcom/yoyicue/chinesechecker/data/ProfileRepository;", "settingsRepository", "Lcom/yoyicue/chinesechecker/data/SettingsRepository;", "getSettingsRepository", "()Lcom/yoyicue/chinesechecker/data/SettingsRepository;", "statsRepository", "Lcom/yoyicue/chinesechecker/data/StatsRepository;", "getStatsRepository", "()Lcom/yoyicue/chinesechecker/data/StatsRepository;", "app_debug"})
public final class AppContainer {
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.db.AppDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.StatsRepository statsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.ProfileRepository profileRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.yoyicue.chinesechecker.data.GameRepository gameRepository = null;
    @org.jetbrains.annotations.Nullable()
    private com.yoyicue.chinesechecker.ui.game.GameConfig lastGameConfig;
    @org.jetbrains.annotations.Nullable()
    private com.yoyicue.chinesechecker.data.GameRepository.Restored pendingRestore;
    
    public AppContainer(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.data.SettingsRepository getSettingsRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.data.StatsRepository getStatsRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.data.ProfileRepository getProfileRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.yoyicue.chinesechecker.data.GameRepository getGameRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.yoyicue.chinesechecker.ui.game.GameConfig getLastGameConfig() {
        return null;
    }
    
    public final void setLastGameConfig(@org.jetbrains.annotations.Nullable()
    com.yoyicue.chinesechecker.ui.game.GameConfig p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.yoyicue.chinesechecker.data.GameRepository.Restored getPendingRestore() {
        return null;
    }
    
    public final void setPendingRestore(@org.jetbrains.annotations.Nullable()
    com.yoyicue.chinesechecker.data.GameRepository.Restored p0) {
    }
}