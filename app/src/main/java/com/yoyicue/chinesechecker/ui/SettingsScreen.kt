package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.ui.LocalAppContainer
import com.yoyicue.chinesechecker.ui.util.HapticKind
import com.yoyicue.chinesechecker.ui.util.rememberHaptic
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    val settings by container.settingsRepository.settings.collectAsState(initial = null)
    val doHaptic = rememberHaptic()

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row { TextButton(onClick = onBack) { Text("返回") } }
        Text("设置")
        if (settings != null) {
            val s = settings!!
            Text("音效音量: %.0f%%".format(s.soundsVolume * 100), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(value = s.soundsVolume, onValueChange = { v -> scope.launch { container.settingsRepository.update { it.copy(soundsVolume = v) } } })

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("震动")
                Switch(
                    checked = s.haptics,
                    onCheckedChange = { on ->
                        if (on) doHaptic(HapticKind.Success)
                        scope.launch { container.settingsRepository.update { it.copy(haptics = on) } }
                    }
                )
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("深色主题")
                Switch(checked = s.themeDark, onCheckedChange = { on ->
                    scope.launch { container.settingsRepository.update { it.copy(themeDark = on) } }
                })
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("快节奏")
                Switch(checked = s.fastGame, onCheckedChange = { on ->
                    scope.launch { container.settingsRepository.update { it.copy(fastGame = on) } }
                })
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Column {
                    Text("启用多格连跳")
                    Text(
                        "所有玩家可连续跳跃更多格数",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(checked = s.longJumps, onCheckedChange = { on ->
                    scope.launch { container.settingsRepository.update { it.copy(longJumps = on) } }
                })
            }

            if (s.fastGame) {
                TurnSecondsDropdown(
                    selected = s.turnSeconds,
                    onSelect = { sec -> scope.launch { container.settingsRepository.update { it.copy(turnSeconds = sec) } } }
                )
                TimeoutActionDropdown(
                    selected = s.timeoutAction,
                    onSelect = { act -> scope.launch { container.settingsRepository.update { it.copy(timeoutAction = act) } } }
                )
            }

            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("调试开关")
                Switch(checked = s.debugOverlay, onCheckedChange = { on ->
                    scope.launch { container.settingsRepository.update { it.copy(debugOverlay = on) } }
                })
            }

            // 默认 AI 难度（用于离线对战页的初始难度）
            Row(modifier = Modifier.padding(top = 16.dp)) { Text("AI 行为") }
            AiDefaultDifficultyDropdown(
                selected = s.aiDifficulty,
                onSelect = { code -> scope.launch { container.settingsRepository.update { it.copy(aiDifficulty = code) } } }
            )
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text("AI 使用多格连跳")
                Switch(checked = s.aiLongJumps, onCheckedChange = { on ->
                    scope.launch { container.settingsRepository.update { it.copy(aiLongJumps = on) } }
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiDefaultDifficultyDropdown(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(
        0 to "弱",
        1 to "中",
        2 to "强"
    )
    val expanded = remember { mutableStateOf(false) }
    val label = options.firstOrNull { it.first == selected }?.second ?: options[1].second
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text("AI 难度") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (code, text) ->
                DropdownMenuItem(text = { Text(text) }, onClick = {
                    onSelect(code)
                    expanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TurnSecondsDropdown(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(10, 15, 20, 30, 45, 60)
    val expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = "$selected 秒",
            onValueChange = {},
            label = { Text("每回合时长") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { sec ->
                DropdownMenuItem(text = { Text("$sec 秒") }, onClick = {
                    onSelect(sec)
                    expanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeoutActionDropdown(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(0 to "超时：跳过回合", 1 to "超时：自动走子")
    val expanded = remember { mutableStateOf(false) }
    val label = options.firstOrNull { it.first == selected }?.second ?: options.first().second
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text("超时行为") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (code, text) ->
                DropdownMenuItem(text = { Text(text) }, onClick = {
                    onSelect(code)
                    expanded.value = false
                })
            }
        }
    }
}
