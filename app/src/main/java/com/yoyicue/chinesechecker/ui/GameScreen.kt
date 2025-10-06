package com.yoyicue.chinesechecker.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import com.yoyicue.chinesechecker.ui.util.HapticKind
import com.yoyicue.chinesechecker.ui.util.rememberHaptic
import com.yoyicue.chinesechecker.ui.util.rememberSfx
import com.yoyicue.chinesechecker.BuildConfig
import com.yoyicue.chinesechecker.ui.game.GameViewModel.SfxEvent
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex
import com.yoyicue.chinesechecker.ui.game.GameViewModel
import com.yoyicue.chinesechecker.ui.game.ClockConfig
import com.yoyicue.chinesechecker.ui.game.TimeoutAction
import kotlin.math.sqrt
import com.yoyicue.chinesechecker.data.AppSettings
import com.yoyicue.chinesechecker.ui.LocalAppContainer
import com.yoyicue.chinesechecker.ui.game.GameConfig
import com.yoyicue.chinesechecker.ui.game.PlayerConfig
import java.util.Random

@Composable
fun GameScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val pendingRestore = container.pendingRestore
    val baseConfig = pendingRestore?.config ?: container.lastGameConfig
    val config: GameConfig = baseConfig ?: GameConfig(
        playerCount = 2,
        players = listOf(
            com.yoyicue.chinesechecker.ui.game.PlayerConfig(Board.PlayerId.A, com.yoyicue.chinesechecker.ui.game.ControllerType.Human, null, Color(0xFFE53935)),
            com.yoyicue.chinesechecker.ui.game.PlayerConfig(Board.PlayerId.B, com.yoyicue.chinesechecker.ui.game.ControllerType.Human, null, Color(0xFF1E88E5))
        )
    )
    val settings by container.settingsRepository.settings.collectAsState(initial = AppSettings())
    val showDebug = settings.debugOverlay
    val scope = rememberCoroutineScope()
    val doHaptic = rememberHaptic()
    val playSfx = rememberSfx()

    val clock = remember(settings) {
        ClockConfig(
            enabled = settings.fastGame,
            turnSeconds = settings.turnSeconds.coerceIn(5, 120),
            timeout = when (settings.timeoutAction) { 1 -> TimeoutAction.AutoMove; else -> TimeoutAction.Skip }
        )
    }
    // Apply long-jump toggle to rules
    androidx.compose.runtime.LaunchedEffect(settings.longJumps) {
        com.yoyicue.chinesechecker.game.Board.setLongJumps(settings.longJumps)
    }
    val vm: GameViewModel = viewModel(factory = viewModelFactory { initializer { GameViewModel(config, clock, settings.aiLongJumps, pendingRestore) } })
    container.pendingRestore = null
    val ui by vm.ui.collectAsState()
    val events by vm.events.collectAsState()
    val canUndo by vm.canUndo.collectAsState(initial = false)
    // SFX listener: gate by soundsVolume > 0
    androidx.compose.runtime.LaunchedEffect(Unit) {
        vm.sfx.collect { ev ->
            if (settings.soundsVolume > 0f) {
                when (ev) {
                    SfxEvent.Select -> playSfx(com.yoyicue.chinesechecker.ui.util.SfxKind.Select)
                    SfxEvent.Invalid -> playSfx(com.yoyicue.chinesechecker.ui.util.SfxKind.Invalid)
                    SfxEvent.Move -> playSfx(com.yoyicue.chinesechecker.ui.util.SfxKind.Move)
                    SfxEvent.Win -> playSfx(com.yoyicue.chinesechecker.ui.util.SfxKind.Win)
                }
            }
        }
    }

    // Active config (restored or initial) for color/name mapping
    val activeConfig = container.lastGameConfig ?: config
    // Color mapping for pieces
    val colorByPlayer: Map<Board.PlayerId, Color> = remember(activeConfig) { activeConfig.players.associate { it.playerId to it.color } }
    val configById: Map<Board.PlayerId, PlayerConfig> = remember(activeConfig) { activeConfig.players.associateBy { it.playerId } }

    // Strong guard: if current board has no pieces (due to any unexpected race/invalid state), reset to a new game
    androidx.compose.runtime.LaunchedEffect(ui.board) {
        val total = ui.board.activePlayers.sumOf { pid -> ui.board.allPieces(pid).size }
        if (total == 0) {
            // Clear any bad save and start fresh to avoid ghost board
            container.gameRepository.clearSave()
            vm.newGame()
        }
    }

    val showConfirmExit = remember { mutableStateOf(false) }
    BackHandler(true) {
        if (showConfirmExit.value) showConfirmExit.value = false else showConfirmExit.value = true
    }

    val showVictoryOverlay = remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(ui.winner) {
        showVictoryOverlay.value = ui.winner != null
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { showConfirmExit.value = true }) { Text("返回") }
                TextButton(onClick = {
                    if (settings.haptics) doHaptic(HapticKind.Light)
                    vm.undo()
                }, enabled = canUndo) { Text("悔棋") }
                
            }
            val current = ui.board.currentPlayer
            val indicatorColor = (ui.winner?.let { colorByPlayer[it] } ?: colorByPlayer[current])
                ?: MaterialTheme.colorScheme.onSurface
            val curCamp = ui.board.startCampOf[current]
            val timeStr = ui.timeLeftSec?.let { " · 剩余 ${it}s" } ?: ""
            val whoText = ui.winner?.let { "胜者: ${it.name} ${campLabel(ui.board.startCampOf[it])}" }
                ?: "轮到: ${current.name} ${campLabel(curCamp)}$timeStr"
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(16.dp).background(indicatorColor))
                Text(text = whoText, color = MaterialTheme.colorScheme.onBackground)
            }
            // 调试按钮移至上层覆盖，避免顶栏拥挤
        }

        // Legend: show each player's color and seat/camp + Human/AI
        LegendRow(
            order = ui.board.activePlayers,
            colors = colorByPlayer,
            camps = ui.board.startCampOf,
            configs = configById
        )

        Box(modifier = Modifier.fillMaxSize()) {
            BoardCanvas(
                board = ui.board,
                selected = ui.selected,
                colors = colorByPlayer,
                lastMovePath = ui.lastMovePath,
                lastMoveOwner = ui.lastMoveOwner,
                animPath = ui.animPath,
                animOwner = ui.animOwner,
                onAnimFinished = { vm.onAnimationFinished() },
                onTapHex = { tapped -> vm.onTap(tapped) }
            )
            if (showDebug) {
                DebugOverlay(events = events)
            }
            // Winner ceremony overlay
            if (showVictoryOverlay.value) {
                val winner = ui.winner ?: Board.PlayerId.A
                val winColor = colorByPlayer[winner] ?: defaultColorFor(winner)
                val colorName = chineseColorName(winColor)
                VictoryOverlay(
                    winner = winner,
                    color = winColor,
                    colorName = colorName,
                    onPlayAgain = {
                        if (settings.haptics) doHaptic(HapticKind.Success)
                        showVictoryOverlay.value = false
                        vm.newGame()
                    },
                    onHome = {
                        showVictoryOverlay.value = false
                        showConfirmExit.value = true
                    }
                )
            }
            if (showDebug) {
                TextButton(
                    onClick = { vm.clearLog() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .alpha(0.5f),
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("清空日志", maxLines = 1, softWrap = false)
                }
            }
        }

        // Haptics: selection, move start, win
        val lastSelected = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<Hex?>(null) }
        androidx.compose.runtime.LaunchedEffect(ui.selected) {
            val cur = ui.selected
            if (settings.haptics && cur != null && cur != lastSelected.value) {
                doHaptic(HapticKind.Light)
            }
            lastSelected.value = cur
        }
        androidx.compose.runtime.LaunchedEffect(ui.animating) {
            if (settings.haptics && ui.animating) {
                doHaptic(HapticKind.Medium)
            }
        }
        androidx.compose.runtime.LaunchedEffect(ui.winner) {
            if (settings.haptics && ui.winner != null) {
                doHaptic(HapticKind.Success)
            }
        }

        if (showConfirmExit.value) {
            AlertDialog(
                onDismissRequest = { showConfirmExit.value = false },
                title = { Text("确认退出对局？") },
                text = { Text("选择是否保存当前对局进度。") },
                confirmButton = {
                    TextButton(onClick = {
                        // 保存并退出
                        scope.launch {
                            container.gameRepository.save(ui.board, ui.lastMovePath, ui.lastMoveOwner, config)
                            showConfirmExit.value = false
                            onBack()
                        }
                    }) { Text("保存并退出") }
                },
                dismissButton = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextButton(onClick = {
                            // 不保存并退出
                            scope.launch {
                                container.gameRepository.clearSave()
                                showConfirmExit.value = false
                                onBack()
                            }
                        }) { Text("不保存并退出") }
                        TextButton(onClick = { showConfirmExit.value = false }) { Text("取消") }
                    }
                }
            )
        }

        // Victory modal removed; we keep only top status text and SFX/Haptics.
    }

    // Persist side effects outside layout to avoid recomposition loops
    // Use up-to-date config (restored or initial)
    PersistSideEffects(container = container, config = activeConfig, ui = ui)
}

@Composable
private fun PersistSideEffects(
    container: com.yoyicue.chinesechecker.data.AppContainer,
    config: GameConfig,
    ui: com.yoyicue.chinesechecker.ui.game.GameUiState
) {
    // Always keep in-progress save up to date when未动画
    androidx.compose.runtime.LaunchedEffect(ui.board, ui.lastMovePath, ui.lastMoveOwner, ui.animating) {
        if (!ui.animating) {
            container.gameRepository.save(ui.board, ui.lastMovePath, ui.lastMoveOwner, config)
        }
    }
    // Record stats once per完成态；悔棋回到未终局会重置
    val recorded = androidx.compose.runtime.remember { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(ui.winner) {
        if (ui.winner == null) {
            recorded.value = false
        } else if (!recorded.value) {
            container.statsRepository.recordGameResult(ui.winner, config)
            container.gameRepository.clearSave()
            recorded.value = true
        }
    }
}

@Composable
private fun BoardCanvas(
    board: Board,
    selected: Hex?,
    colors: Map<Board.PlayerId, Color>,
    lastMovePath: List<Hex>?,
    lastMoveOwner: Board.PlayerId?,
    animPath: List<Hex>?,
    animOwner: Board.PlayerId?,
    onAnimFinished: () -> Unit,
    onTapHex: (Hex) -> Unit
) {
    // Theme palette snapshot for drawing (capture outside Canvas draw lambda)
    val scheme = MaterialTheme.colorScheme
    // Animations: pulse endpoints and selected piece glow
    val transition = rememberInfiniteTransition(label = "pulse")
    val endpointPulse = transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "endpointPulse"
    )
    val selectedPulseAlpha = transition.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "selectedPulseAlpha"
    )

    // Precompute normalized positions with size=1 for fitting
    val pts = remember(board.positions) { board.positions.toList() }
    val axial = remember(pts) { pts.map { it.x to it.z } } // q=x, r=z
    val unitPx = remember(axial) { axial.map { toPointyPixel(it.first.toFloat(), it.second.toFloat(), 1f) } }
    val minX = unitPx.minOf { it.x }
    val maxX = unitPx.maxOf { it.x }
    val minY = unitPx.minOf { it.y }
    val maxY = unitPx.maxOf { it.y }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .pointerInput(board, selected) {
                detectTapGestures { offset ->
                    // Inverse-map by nearest neighbor search
                    val sizePx = size
                    val w = sizePx.width
                    val h = sizePx.height
                    val nodeRadiusUnit = 0.45f // in unit-pixel space (size=1)
                    val scale = 0.95f * minOf(
                        w / (maxX - minX + 2f * nodeRadiusUnit),
                        h / (maxY - minY + 2f * nodeRadiusUnit)
                    )
                    val ox = w / 2f - (minX + maxX) / 2f * scale
                    val oy = h / 2f - (minY + maxY) / 2f * scale

                    // Find nearest node within threshold
                    var bestIndex = -1
                    var bestDist = Float.MAX_VALUE
                    unitPx.forEachIndexed { idx, p ->
                        val px = ox + p.x * scale
                        val py = oy + p.y * scale
                        val dx = px - offset.x
                        val dy = py - offset.y
                        val d2 = dx * dx + dy * dy
                        if (d2 < bestDist) {
                            bestDist = d2
                            bestIndex = idx
                        }
                    }
                    if (bestIndex >= 0) {
                        val radius = scale * nodeRadiusUnit
                        if (bestDist <= (radius * radius * 2.25f)) {
                            onTapHex(pts[bestIndex])
                        }
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val nodeRadiusUnit = 0.45f // in unit-pixel space (size=1)
        val scale = 0.95f * minOf(
            w / (maxX - minX + 2f * nodeRadiusUnit),
            h / (maxY - minY + 2f * nodeRadiusUnit)
        )
        val ox = w / 2f - (minX + maxX) / 2f * scale
        val oy = h / 2f - (minY + maxY) / 2f * scale

        val nodeRadius = scale * nodeRadiusUnit

        // Precompute endpoints for selection
        val endpoints = if (selected != null) board.legalMovesFrom(selected).map { it.to }.toSet() else emptySet()
        val playerColor = colors[board.currentPlayer] ?: scheme.primary

        // Draw board nodes
        pts.forEachIndexed { idx, hHex ->
            val p = unitPx[idx]
            val cx = ox + p.x * scale
            val cy = oy + p.y * scale
            val isEndpoint = hHex in endpoints
            val inCenter = maxOf(kotlin.math.abs(hHex.x), kotlin.math.abs(hHex.y), kotlin.math.abs(hHex.z)) <= 4
            val top = (hHex.z < -4 && kotlin.math.abs(hHex.x) <= 4 && kotlin.math.abs(hHex.y) <= 4)
            val bottom = (hHex.z > 4 && kotlin.math.abs(hHex.x) <= 4 && kotlin.math.abs(hHex.y) <= 4)
            val ne = (hHex.x > 4 && kotlin.math.abs(hHex.y) <= 4 && kotlin.math.abs(hHex.z) <= 4)
            val sw = (hHex.x < -4 && kotlin.math.abs(hHex.y) <= 4 && kotlin.math.abs(hHex.z) <= 4)
            val nw = (hHex.y > 4 && kotlin.math.abs(hHex.x) <= 4 && kotlin.math.abs(hHex.z) <= 4)
            val se = (hHex.y < -4 && kotlin.math.abs(hHex.x) <= 4 && kotlin.math.abs(hHex.z) <= 4)

            val regionColor = when {
                inCenter -> scheme.surfaceVariant
                top -> scheme.primaryContainer
                bottom -> scheme.secondaryContainer
                ne -> scheme.tertiaryContainer
                sw -> scheme.tertiaryContainer
                nw -> scheme.secondaryContainer
                se -> scheme.primaryContainer
                else -> scheme.surface
            }

            val baseColor = when {
                hHex == selected -> scheme.secondary
                else -> regionColor
            }
            drawCircle(color = baseColor, radius = nodeRadius, center = Offset(cx, cy))

            // Endpoint highlight follows current player's color with pulsing
            if (isEndpoint) {
                val pulse = endpointPulse.value
                drawCircle(
                    color = playerColor.copy(alpha = 0.18f),
                    radius = nodeRadius * (1.05f * pulse),
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = playerColor,
                    radius = nodeRadius * (0.95f * pulse),
                    center = Offset(cx, cy),
                    style = Stroke(width = nodeRadius * 0.15f)
                )
            }

            // Draw pieces on top
            val owner = board.at(hHex)
            if (owner != null) {
                val pc = colors[owner] ?: defaultColorFor(owner)
                val isSelectedPiece = (hHex == selected)
                if (isSelectedPiece) {
                    // Outer glow under the piece in its own color (pulsing alpha)
                    drawCircle(color = pc.copy(alpha = selectedPulseAlpha.value), radius = nodeRadius * 1.25f, center = Offset(cx, cy))
                }
                val pieceRadius = if (isSelectedPiece) nodeRadius * 0.88f else nodeRadius * 0.82f
                drawCircle(color = pc, radius = pieceRadius, center = Offset(cx, cy))
                if (isSelectedPiece) {
                    // Bright ring on top
                    drawCircle(
                        color = scheme.onSurface.copy(alpha = 0.9f),
                        radius = nodeRadius * 0.95f,
                        center = Offset(cx, cy),
                        style = Stroke(width = nodeRadius * 0.18f)
                    )
                }
            }

            // Last move path highlighting rings
            if (lastMovePath != null && lastMovePath.contains(hHex)) {
                val lmColor = (lastMoveOwner?.let { colors[it] } ?: scheme.secondary)
                drawCircle(
                    color = lmColor.copy(alpha = 0.18f),
                    radius = nodeRadius * 1.15f,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = lmColor,
                    radius = nodeRadius * 0.95f,
                    center = Offset(cx, cy),
                    style = Stroke(width = nodeRadius * 0.12f)
                )
            }
        }
    }

    // Animation overlay: move a ghost piece along animPath and call onAnimFinished at the end
    if (animPath != null && animPath.size >= 2) {
        val steps = animPath.windowed(2)
        val progress = remember(animPath) { androidx.compose.animation.core.Animatable(0f) }
        val segmentIndex = remember(animPath) { androidx.compose.runtime.mutableStateOf(0) }
        androidx.compose.runtime.LaunchedEffect(animPath) {
            for (i in steps.indices) {
                segmentIndex.value = i
                progress.snapTo(0f)
                progress.animateTo(1f, tween(durationMillis = 200, easing = FastOutSlowInEasing))
            }
            onAnimFinished()
        }

        Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            val w2 = size.width
            val h2 = size.height
            val nodeRadiusUnit2 = 0.45f
            val scale2 = 0.95f * minOf(
                w2 / (maxX - minX + 2f * nodeRadiusUnit2),
                h2 / (maxY - minY + 2f * nodeRadiusUnit2)
            )
            val ox2 = w2 / 2f - (minX + maxX) / 2f * scale2
            val oy2 = h2 / 2f - (minY + maxY) / 2f * scale2
            val nodeRadius2 = scale2 * nodeRadiusUnit2

            val indexByHex = pts.withIndex().associate { it.value to it.index }
            fun center2(hh: Hex): Offset {
                val id = indexByHex[hh] ?: return Offset.Zero
                val p = unitPx[id]
                return Offset(ox2 + p.x * scale2, oy2 + p.y * scale2)
            }
            val i = segmentIndex.value
            if (i in steps.indices) {
                val (a, b) = steps[i]
                val t = progress.value
                val ca = center2(a)
                val cb = center2(b)
                val pos = Offset(ca.x + (cb.x - ca.x) * t, ca.y + (cb.y - ca.y) * t)
                val ownerColor = (animOwner?.let { colors[it] } ?: scheme.onSurface)
                drawCircle(color = ownerColor.copy(alpha = 0.25f), radius = nodeRadius2 * 0.6f, center = pos)
                drawCircle(color = ownerColor, radius = nodeRadius2 * 0.82f, center = pos)
            }
        }
    }
}

private fun toPointyPixel(q: Float, r: Float, size: Float): Offset {
    val x = size * sqrt(3f) * (q + r / 2f)
    val y = size * (3f / 2f) * r
    return Offset(x, y)
}

@Composable
private fun BoxScope.DebugOverlay(events: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(8.dp)
    ) {
        Text(
            text = "调试日志 (${events.size}) - 最近 50 条",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.66f))
                .padding(6.dp)
        )
        val maxShown = 12
        val tail = if (events.size > maxShown) events.takeLast(maxShown) else events
        tail.forEach { line ->
            Text(
                text = line,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun LegendRow(
    order: List<Board.PlayerId>,
    colors: Map<Board.PlayerId, Color>,
    camps: Map<Board.PlayerId, Board.Camp>,
    configs: Map<Board.PlayerId, PlayerConfig>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        order.forEach { pid ->
            val color = colors[pid] ?: MaterialTheme.colorScheme.onSurfaceVariant
            val camp = camps[pid]
            val cfg = configs[pid]
            val role = when (cfg?.controller) {
                com.yoyicue.chinesechecker.ui.game.ControllerType.AI -> when (cfg.difficulty) {
                    com.yoyicue.chinesechecker.game.AiDifficulty.Weak -> "AI-弱"
                    com.yoyicue.chinesechecker.game.AiDifficulty.Greedy -> "AI-中"
                    com.yoyicue.chinesechecker.game.AiDifficulty.Smart -> "AI-强"
                    null -> "AI"
                }
                com.yoyicue.chinesechecker.ui.game.ControllerType.Human -> "玩家"
                null -> "玩家"
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(12.dp).background(color))
                Text(text = "${pid.name} ${campLabel(camp)} $role", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private fun campLabel(camp: Board.Camp?): String = when (camp) {
    Board.Camp.Top -> "上"
    Board.Camp.Bottom -> "下"
    Board.Camp.NE -> "右上"
    Board.Camp.SE -> "右下"
    Board.Camp.SW -> "左下"
    Board.Camp.NW -> "左上"
    null -> ""
}

private fun chineseColorName(c: Color): String {
    val palette = listOf(
        Color(0xFFE53935) to "红色",
        Color(0xFF1E88E5) to "蓝色",
        Color(0xFF8E24AA) to "紫色",
        Color(0xFF43A047) to "绿色",
        Color(0xFFFDD835) to "黄色",
        Color(0xFFFF7043) to "橙色",
        Color(0xFF26A69A) to "青色"
    )
    val (r, g, b) = rgb(c)
    var best = Int.MAX_VALUE
    var name = "自定义"
    for ((col, label) in palette) {
        val (pr, pg, pb) = rgb(col)
        val dr = r - pr; val dg = g - pg; val db = b - pb
        val d2 = dr * dr + dg * dg + db * db
        if (d2 < best) { best = d2; name = label }
    }
    return name
}

private fun rgb(c: Color): Triple<Int, Int, Int> {
    val v = c.toArgb()
    val r = (v shr 16) and 0xFF
    val g = (v shr 8) and 0xFF
    val b = (v) and 0xFF
    return Triple(r, g, b)
}

private fun defaultColorFor(pid: Board.PlayerId): Color = when (pid) {
    Board.PlayerId.A -> Color(0xFFE53935) // red
    Board.PlayerId.B -> Color(0xFF1E88E5) // blue
    Board.PlayerId.C -> Color(0xFF8E24AA) // purple
    Board.PlayerId.D -> Color(0xFF43A047) // green
    Board.PlayerId.E -> Color(0xFFFDD835) // yellow
    Board.PlayerId.F -> Color(0xFFFF7043) // orange
}

@Composable
private fun VictoryOverlay(
    winner: Board.PlayerId,
    color: Color,
    colorName: String,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    // Build a colorful palette mixing theme colors and winner color
    val palette = remember(color, scheme) {
        listOf(
            color,
            scheme.primary,
            scheme.secondary,
            scheme.tertiary,
            scheme.primaryContainer,
            scheme.secondaryContainer,
            scheme.tertiaryContainer
        )
    }
    // Particles parameters
    data class Particle(
        val xFrac: Float,
        val phase: Float,
        val speed: Float,
        val sizePx: Float,
        val rotSpeed: Float,
        val col: Color
    )
    val rnd = remember { Random() }
    val particles = remember {
        List(120) {
            Particle(
                xFrac = rnd.nextFloat(),
                phase = rnd.nextFloat(),
                speed = 0.35f + rnd.nextFloat() * 0.9f,
                sizePx = 6f + rnd.nextFloat() * 14f,
                rotSpeed = -90f + rnd.nextFloat() * 180f,
                col = palette[rnd.nextInt(palette.size)]
            )
        }
    }
    val t by rememberInfiniteTransition(label = "confetti").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 3500, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "t"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Confetti canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            particles.forEach { p ->
                val yy = ((t * p.speed + p.phase) % 1f) * (h + 40f) - 20f
                val xx = p.xFrac * w + kotlin.math.sin((t + p.phase) * 6.283f) * 18f
                val rot = (t * p.rotSpeed * 360f) % 360f
                rotate(degrees = rot, pivot = Offset(xx, yy)) {
                    drawRect(color = p.col, topLeft = Offset(xx - p.sizePx / 2f, yy - p.sizePx / 2f), size = Size(p.sizePx, p.sizePx * 0.6f))
                }
            }
        }

        // Celebration card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(24.dp)
                .background(scheme.surface.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("🏆", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = "${winner.name} ${colorName} 获胜！",
                style = MaterialTheme.typography.headlineMedium,
                color = scheme.onSurface
            )
            Text(
                text = "恭喜！精彩对局！",
                style = MaterialTheme.typography.titleSmall,
                color = scheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onPlayAgain) { Text("再来一局") }
                Button(onClick = onHome) { Text("返回主页") }
            }
        }
    }
}
