package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.game.AiDifficulty
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.ui.LocalAppContainer

@Composable
fun OfflineConfigScreen(
    onBack: () -> Unit,
    onStartGame: () -> Unit
) {
    val container = LocalAppContainer.current
    val viewModel: com.yoyicue.chinesechecker.ui.offline.OfflineConfigViewModel = viewModel(factory = viewModelFactory {
        initializer {
            com.yoyicue.chinesechecker.ui.offline.OfflineConfigViewModel(
                settingsRepository = container.settingsRepository,
                gameRepository = container.gameRepository,
                appContainer = container
            )
        }
    })
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row { TextButton(onClick = onBack) { Text("返回") } }
        Text("离线对战配置", style = MaterialTheme.typography.headlineMedium)

        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .verticalScroll(scrollState)
                .padding(top = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("玩家人数")
                CountDropdown(
                    options = listOf(2, 3, 4, 6),
                    selected = state.playerCount,
                    onSelect = viewModel::setPlayerCount
                )
            }

            Column(modifier = Modifier.padding(top = 8.dp)) {
                state.seats.forEach { (pid, label) ->
                    val setup = viewModel.currentSetup(pid) ?: return@forEach
                    val optionsForThis = viewModel.colorOptionsFor(pid).ifEmpty { com.yoyicue.chinesechecker.ui.offline.OfflineConfigViewModel.COLOR_PALETTE }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0x0DFFFFFF))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "$label (${pid.name})")
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("AI")
                                Switch(checked = setup.isAi, onCheckedChange = { on -> viewModel.toggleAi(pid, on) })
                            }
                            Row(
                                modifier = Modifier.padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (setup.isAi) {
                                    AiDropdown(
                                        selected = setup.difficulty,
                                        onSelect = { diff -> viewModel.setDifficulty(pid, diff) }
                                    )
                                }
                                ColorDropdown(
                                    options = optionsForThis,
                                    selected = setup.color,
                                    onSelect = { color -> viewModel.setColor(pid, color) }
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = {
                val config = viewModel.createGameConfig()
                viewModel.prepareGame(config)
                onStartGame()
            }
        ) { Text("开始游戏") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountDropdown(options: List<Int>, selected: Int, onSelect: (Int) -> Unit) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = selected.toString(),
            onValueChange = {},
            label = { Text("人数") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { opt ->
                DropdownMenuItem(text = { Text(opt.toString()) }, onClick = {
                    onSelect(opt)
                    expanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiDropdown(selected: AiDifficulty, onSelect: (AiDifficulty) -> Unit) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val options = listOf(AiDifficulty.Weak, AiDifficulty.Greedy, AiDifficulty.Smart)
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = when (selected) {
                AiDifficulty.Weak -> "弱"
                AiDifficulty.Greedy -> "中"
                AiDifficulty.Smart -> "强"
            },
            onValueChange = {},
            label = { Text("AI 难度") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor().width(120.dp)
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { opt ->
                val label = when (opt) {
                    AiDifficulty.Weak -> "弱"
                    AiDifficulty.Greedy -> "中"
                    AiDifficulty.Smart -> "强"
                }
                DropdownMenuItem(text = { Text(label) }, onClick = {
                    onSelect(opt)
                    expanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorDropdown(options: List<Pair<Color, String>>, selected: Color, onSelect: (Color) -> Unit) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val label = options.firstOrNull { it.first == selected }?.second
        ?: options.firstOrNull()?.second
        ?: "颜色"
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text("颜色") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor().width(100.dp)
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (c, name) ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(18.dp).background(c))
                            Text(text = name, modifier = Modifier.padding(start = 8.dp))
                        }
                    },
                    onClick = {
                        onSelect(c)
                        expanded.value = false
                    }
                )
            }
        }
    }
}
