package com.yoyicue.chinesechecker.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex
import kotlin.math.sqrt

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    val tabTitles = listOf("基础规则", "AI 介绍")
    val selectedTab = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row { TextButton(onClick = onBack) { Text("返回") } }
        Text("怎么玩", style = MaterialTheme.typography.headlineMedium)
        TabRow(selectedTabIndex = selectedTab.value, modifier = Modifier.padding(top = 16.dp)) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab.value) {
            0 -> RulesTabContent()
            else -> AiGuideContent(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
private fun RulesTabContent() {
    val restartTick = remember { mutableStateOf(0) }
    val demoMode = remember { mutableStateOf(DemoMode.Normal) }

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Text("标准规则", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("- 一步走：移动到与棋子相邻（距离为1）的空位。")
            Text("- 普通跳：跨过相邻棋子，落到其正对的空位。")
            Text("- 连续跳：一次行动内可连续多次普通跳，每次落点必须为空。")
            Text("- 胜利条件：把所有棋子移动到目标营地。")
        }

        Text("长跳规则", modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("- 启用方式：设置 → 启用多格连跳。")
            Text("- 规则含义：允许对称跨越距离更远的棋子（镜像跨多格），落点与被跨棋子到原位置的距离相等且必须为空。")
            Text("- 连续跳跃：长跳与普通跳可在同一回合串联使用，只要每段落点合法。")
        }

        Text("演示：连跳路径（示意）", modifier = Modifier.padding(top = 16.dp), style = MaterialTheme.typography.titleMedium)
        val demoTabs = listOf("普通跳", "长跳")
        TabRow(selectedTabIndex = if (demoMode.value == DemoMode.Normal) 0 else 1, modifier = Modifier.padding(top = 8.dp)) {
            demoTabs.forEachIndexed { index, title ->
                val mode = if (index == 0) DemoMode.Normal else DemoMode.Long
                Tab(
                    selected = demoMode.value == mode,
                    onClick = {
                        if (demoMode.value != mode) {
                            demoMode.value = mode
                            restartTick.value++
                        }
                    },
                    text = { Text(title) }
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
            DemoBoardPath(restartTick.value, demoMode.value)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = { restartTick.value++ }) { Text("重新播放") }
        }
        Text(
            text = if (demoMode.value == DemoMode.Normal)
                "普通跳示例：红色棋子依次跨过多枚紧邻棋子，逐步向前推进。"
            else
                "长跳示例：红色棋子一次跨过与自身对称排列的蓝色棋子，并继续连跳。",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun DemoBoardPath(restartKey: Int, mode: DemoMode) {
    val board = remember { Board.standard() }
    val pts = remember(board.positions) { board.positions.toList() }
    val axial = remember(pts) { pts.map { it.x to it.z } }
    val unitPx = remember(axial) { axial.map { toPointyPixel(it.first.toFloat(), it.second.toFloat(), 1f) } }
    val minX = unitPx.minOf { it.x }
    val maxX = unitPx.maxOf { it.x }
    val minY = unitPx.minOf { it.y }
    val maxY = unitPx.maxOf { it.y }

    val path = remember(restartKey, mode) {
        when (mode) {
            DemoMode.Normal -> listOf(
                Hex(-4, 0, 4),
                Hex(-3, 0, 3),
                Hex(-1, 0, 1),
                Hex(1, 0, -1),
                Hex(3, 0, -3)
            )
            DemoMode.Long -> listOf(
                Hex(-4, 0, 4),  // 起点
                Hex(0, -4, 4),  // 镜像长跳落点
                Hex(2, -4, 2)   // 接着再做一次普通跳
            )
        }
    }

    val staticPieces = remember(mode) {
        when (mode) {
            DemoMode.Normal -> setOf(
                Hex(-2, 0, 2),
                Hex(0, 0, 0),
                Hex(2, 0, -2)
            )
            DemoMode.Long -> setOf(
                Hex(-2, -2, 4), // 长跳被跨棋子（镜像）
                Hex(1, -4, 3)   // 后续普通跳的被跨棋子
            )
        }
    }

    // Animation progress per segment
    val animFrac = remember { androidx.compose.animation.core.Animatable(0f) }
    val segIndex = remember(restartKey) { mutableStateOf(0) }
    LaunchedEffect(restartKey) {
        for (i in 0 until path.size - 1) {
            segIndex.value = i
            animFrac.snapTo(0f)
            animFrac.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }
    }

    val scheme = MaterialTheme.colorScheme
    Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
        val w = size.width
        val heightPx = size.height
        val nodeRadiusUnit = 0.45f
        val scale = 0.9f * minOf(
            w / (maxX - minX + 2f * nodeRadiusUnit),
            heightPx / (maxY - minY + 2f * nodeRadiusUnit)
        )
        val ox = w / 2f - (minX + maxX) / 2f * scale
        val oy = heightPx / 2f - (minY + maxY) / 2f * scale
        val nodeRadius = scale * nodeRadiusUnit

        // Draw nodes (pale)
        pts.forEachIndexed { idx, _ ->
            val p = unitPx[idx]
            val cx = ox + p.x * scale
            val cy = oy + p.y * scale
            drawCircle(color = scheme.surfaceVariant, radius = nodeRadius * 0.9f, center = Offset(cx, cy))
        }

        val staticColor = Color(0xFF1E88E5)
        staticPieces.forEach { hex ->
            val id = pts.indexOf(hex)
            if (id >= 0) {
                val p = unitPx[id]
                val cx = ox + p.x * scale
                val cy = oy + p.y * scale
                drawCircle(color = staticColor, radius = nodeRadius * 0.75f, center = Offset(cx, cy))
            }
        }

        // Draw animated dot along path
        fun centerOf(hh: Hex): Offset {
            val id = pts.indexOf(hh)
            val p = unitPx[id]
            return Offset(ox + p.x * scale, oy + p.y * scale)
        }
        val i = segIndex.value
        val frac = animFrac.value
        val a = centerOf(path[i])
        val b = centerOf(path[i + 1])
        val pos = Offset(a.x + (b.x - a.x) * frac, a.y + (b.y - a.y) * frac)
        drawCircle(color = Color(0xFFE53935), radius = nodeRadius * 0.82f, center = pos)
    }
}

private enum class DemoMode { Normal, Long }

private fun toPointyPixel(q: Float, r: Float, size: Float): Offset {
    val x = size * sqrt(3f) * (q + r / 2f)
    val y = size * (3f / 2f) * r
    return Offset(x, y)
}
