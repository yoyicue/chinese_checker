package com.yoyicue.chinesechecker.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a$\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001aH\u0010\u0006\u001a\u00020\u00012\u0018\u0010\u0007\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t0\b2\u0006\u0010\u0002\u001a\u00020\n2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a2\u0010\u000e\u001a\u00020\u00012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u000f0\b2\u0006\u0010\u0002\u001a\u00020\u000f2\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a$\u0010\u0010\u001a\u00020\u00012\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u0012H\u0007\u001a\"\u0010\u0014\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u000b0\t0\b2\u0006\u0010\u0016\u001a\u00020\u000fH\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0017"}, d2 = {"AiDropdown", "", "selected", "Lcom/yoyicue/chinesechecker/game/AiDifficulty;", "onSelect", "Lkotlin/Function1;", "ColorDropdown", "options", "", "Lkotlin/Pair;", "Landroidx/compose/ui/graphics/Color;", "", "ColorDropdown-bw27NRU", "(Ljava/util/List;JLkotlin/jvm/functions/Function1;)V", "CountDropdown", "", "OfflineConfigScreen", "onBack", "Lkotlin/Function0;", "onStartGame", "seatsForCount", "Lcom/yoyicue/chinesechecker/game/Board$PlayerId;", "n", "app_debug"})
public final class OfflineConfigScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void OfflineConfigScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onStartGame) {
    }
    
    private static final java.util.List<kotlin.Pair<com.yoyicue.chinesechecker.game.Board.PlayerId, java.lang.String>> seatsForCount(int n) {
        return null;
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void CountDropdown(java.util.List<java.lang.Integer> options, int selected, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onSelect) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void AiDropdown(com.yoyicue.chinesechecker.game.AiDifficulty selected, kotlin.jvm.functions.Function1<? super com.yoyicue.chinesechecker.game.AiDifficulty, kotlin.Unit> onSelect) {
    }
}