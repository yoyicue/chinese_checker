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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import com.yoyicue.chinesechecker.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex
import kotlin.math.sqrt

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    val tabs = listOf(R.string.howto_tab_rules, R.string.howto_tab_ai)
    val selectedTab = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Row { TextButton(onClick = onBack) { Text(stringResource(R.string.common_back)) } }
        Text(stringResource(R.string.howto_title), style = MaterialTheme.typography.headlineMedium)
        TabRow(selectedTabIndex = selectedTab.value, modifier = Modifier.padding(top = 16.dp)) {
            tabs.forEachIndexed { index, titleRes ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index },
                    text = { Text(stringResource(titleRes)) }
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
        Text(
            stringResource(R.string.howto_rules_standard_heading),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            stringResource(R.string.howto_rules_standard),
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            stringResource(R.string.howto_rules_long_heading),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            stringResource(R.string.howto_rules_long_jump),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            stringResource(R.string.howto_rules_layout),
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            stringResource(R.string.howto_demo_title),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        val demoTabs = listOf(DemoMode.Normal to R.string.howto_demo_normal, DemoMode.Long to R.string.howto_demo_long)
        TabRow(selectedTabIndex = if (demoMode.value == DemoMode.Normal) 0 else 1, modifier = Modifier.padding(top = 8.dp)) {
            demoTabs.forEach { (mode, titleRes) ->
                Tab(
                    selected = demoMode.value == mode,
                    onClick = {
                        if (demoMode.value != mode) {
                            demoMode.value = mode
                            restartTick.value++
                        }
                    },
                    text = { Text(stringResource(titleRes)) }
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
            DemoBoardPath(restartTick.value, demoMode.value)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = { restartTick.value++ }) { Text(stringResource(R.string.howto_demo_restart)) }
        }
        val descriptionRes = if (demoMode.value == DemoMode.Normal) {
            R.string.howto_demo_normal_desc
        } else {
            R.string.howto_demo_long_desc
        }
        Text(
            text = stringResource(descriptionRes),
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
                Hex(-4, 0, 4),  // start
                Hex(0, -4, 4),  // mirrored long-jump landing
                Hex(2, -4, 2)   // followed by a normal jump
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
                Hex(-2, -2, 4), // mirrored piece that is jumped over during long jump
                Hex(1, -4, 3)   // piece jumped over during the follow-up normal jump
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
