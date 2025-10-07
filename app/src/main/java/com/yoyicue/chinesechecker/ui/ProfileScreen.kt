package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.ui.LocalAppContainer
import com.yoyicue.chinesechecker.ui.profile.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory {
        initializer { ProfileViewModel(container.profileRepository, container.statsRepository) }
    })
    val stats by viewModel.stats.collectAsState(initial = null)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row { TextButton(onClick = onBack) { Text("返回") } }
        Text("统计", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))
        Text("胜负统计", style = MaterialTheme.typography.titleMedium)
        if (stats != null) {
            val s = stats!!
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("总局数: ${s.totalGames}")
                Text("最近游玩: ${formatTimestamp(s.lastPlayedAt)}")
                Spacer(Modifier.height(12.dp))
                StatsBarChart(
                    title = "按玩家胜场",
                    data = s.winsByPlayer
                )
                Spacer(Modifier.height(12.dp))
                StatsBarChart(
                    title = "按类型胜场（Human/AI）",
                    data = s.winsByController
                )
                Spacer(Modifier.height(12.dp))
                val mappedDifficulty = s.winsByDifficulty.mapKeys { (k, _) ->
                    when (k) {
                        "Weak" -> "弱"
                        "Greedy" -> "中"
                        "Smart" -> "强"
                        else -> k
                    }
                }
                StatsBarChart(
                    title = "按难度胜场",
                    data = mappedDifficulty
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = { viewModel.resetStats() }) { Text("重置统计") }
        } else {
            Text("统计加载中…")
        }
    }
}

@Composable
private fun StatsBarChart(title: String, data: Map<String, Int>, barHeight: Dp = 18.dp) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        if (data.isEmpty()) {
            Text("(暂无)", color = MaterialTheme.colorScheme.onSurfaceVariant)
            return@Column
        }
        val sorted = data.entries.sortedByDescending { it.value }
        val maxVal = sorted.first().value.coerceAtLeast(1)
        val palette = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.secondaryContainer
        )
        sorted.forEachIndexed { idx, (label, value) ->
            val frac = value.toFloat() / maxVal
            val color = palette[idx % palette.size]
            StatsBarRow(label = label, value = value, fraction = frac, color = color, barHeight = barHeight)
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun StatsBarRow(label: String, value: Int, fraction: Float, color: Color, barHeight: Dp) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value.toString(), color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(4.dp))
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .height(barHeight)
                    .background(color)
            )
        }
    }
}

private fun formatTimestamp(ts: Long?): String {
    if (ts == null || ts <= 0L) return "-"
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        sdf.format(Date(ts))
    } catch (t: Throwable) { ts.toString() }
}
