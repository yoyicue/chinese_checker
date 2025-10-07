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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.R
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
        Row { TextButton(onClick = onBack) { Text(stringResource(R.string.common_back)) } }
        Text(stringResource(R.string.offline_title), style = MaterialTheme.typography.headlineMedium)

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
                Text(stringResource(R.string.offline_player_count))
                CountDropdown(
                    options = listOf(2, 3, 4, 6),
                    selected = state.playerCount,
                    onSelect = viewModel::setPlayerCount
                )
            }

            Column(modifier = Modifier.padding(top = 8.dp)) {
                state.seats.forEach { pid ->
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
                        Text(text = "${seatLabel(pid)} (${pid.name})")
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(stringResource(R.string.offline_ai_label))
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
        ) { Text(stringResource(R.string.offline_start_game)) }
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
            label = { Text(stringResource(R.string.offline_player_count)) },
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
                AiDifficulty.Weak -> stringResource(R.string.difficulty_easy)
                AiDifficulty.Greedy -> stringResource(R.string.difficulty_medium)
                AiDifficulty.Smart -> stringResource(R.string.difficulty_hard)
            },
            onValueChange = {},
            label = { Text(stringResource(R.string.offline_ai_level)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor().width(120.dp)
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { opt ->
                val label = when (opt) {
                    AiDifficulty.Weak -> stringResource(R.string.difficulty_easy)
                    AiDifficulty.Greedy -> stringResource(R.string.difficulty_medium)
                    AiDifficulty.Smart -> stringResource(R.string.difficulty_hard)
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
private fun ColorDropdown(options: List<Pair<Color, Int>>, selected: Color, onSelect: (Color) -> Unit) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val labelRes = options.firstOrNull { it.first == selected }?.second
        ?: options.firstOrNull()?.second
        ?: R.string.offline_color_label
    val label = stringResource(labelRes)
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = !expanded.value }) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text(stringResource(R.string.offline_color_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor().width(100.dp)
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (c, nameRes) ->
                val name = stringResource(nameRes)
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

@Composable
private fun seatLabel(pid: Board.PlayerId): String = when (pid) {
    Board.PlayerId.A -> stringResource(R.string.seat_top)
    Board.PlayerId.B -> stringResource(R.string.seat_bottom)
    Board.PlayerId.C -> stringResource(R.string.seat_ne)
    Board.PlayerId.D -> stringResource(R.string.seat_sw)
    Board.PlayerId.E -> stringResource(R.string.seat_nw)
    Board.PlayerId.F -> stringResource(R.string.seat_se)
}