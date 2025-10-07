package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import com.yoyicue.chinesechecker.ui.LocalAppContainer
import com.yoyicue.chinesechecker.ui.util.HapticKind
import com.yoyicue.chinesechecker.ui.util.rememberHaptic
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val viewModel: SettingsViewModel = viewModel(factory = viewModelFactory {
        initializer { SettingsViewModel(container.settingsRepository) }
    })
    val settings by viewModel.settings.collectAsState()
    val doHaptic = rememberHaptic()
    val scrollState = rememberScrollState()
    var secretTapCount by remember { mutableStateOf(0) }
    var debugUnlocked by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row { TextButton(onClick = onBack) { Text("返回") } }
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.clickable {
                if (!debugUnlocked) {
                    secretTapCount += 1
                    if (secretTapCount >= 10) {
                        debugUnlocked = true
                    }
                }
            }
        )
        val s = settings
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Text(
                "音效音量: %.0f%%".format(s.soundsVolume * 100),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = s.soundsVolume,
                onValueChange = { v -> viewModel.setSoundsVolume(v) },
                modifier = Modifier.fillMaxWidth()
            )

            SettingSwitchRow(
                title = "震动",
                checked = s.haptics,
                onCheckedChange = { on ->
                    if (on) doHaptic(HapticKind.Success)
                    viewModel.setHaptics(on)
                }
            )

            SettingSwitchRow(
                title = "深色主题",
                checked = s.themeDark,
                onCheckedChange = viewModel::setThemeDark
            )

            SettingSwitchRow(
                title = "限时模式",
                description = "为每位玩家开启走棋倒计时。",
                checked = s.fastGame,
                onCheckedChange = viewModel::setFastGame
            )

            SettingSwitchRow(
                title = "启用多格连跳",
                description = "允许对称跨越更多棋子，连续跳跃时可产生更远位移。",
                checked = s.longJumps,
                onCheckedChange = viewModel::setLongJumps
            )

            if (s.fastGame) {
                Spacer(Modifier.height(8.dp))
                TurnSecondsDropdown(
                    selected = s.turnSeconds,
                    onSelect = viewModel::setTurnSeconds
                )
                Spacer(Modifier.height(8.dp))
                TimeoutActionDropdown(
                    selected = s.timeoutAction,
                    onSelect = viewModel::setTimeoutAction
                )
            }

            Spacer(Modifier.height(12.dp))
            Text("AI 行为", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            AiDefaultDifficultyDropdown(
                selected = s.aiDifficulty,
                onSelect = viewModel::setAiDifficulty
            )
            SettingSwitchRow(
                title = "AI 使用多格连跳",
                checked = s.aiLongJumps,
                onCheckedChange = viewModel::setAiLongJumps
            )

            val debugVisible = debugUnlocked || s.debugOverlay
            if (debugVisible) {
                Spacer(Modifier.height(24.dp))
                SettingSwitchRow(
                    title = "调试开关",
                    description = "在对局中显示额外日志与事件信息。",
                    checked = s.debugOverlay,
                    onCheckedChange = viewModel::setDebugOverlay
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
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
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
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
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
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
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
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
