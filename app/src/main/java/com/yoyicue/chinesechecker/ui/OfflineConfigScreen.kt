package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yoyicue.chinesechecker.R
import com.yoyicue.chinesechecker.game.AiDifficulty
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.ui.LocalAppContainer
import com.yoyicue.chinesechecker.ui.offline.OfflineConfigViewModel.ColorOption

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
                    val optionsForThis = viewModel.colorOptionsFor(pid)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0x0DFFFFFF))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${seatLabel(pid)} (${pid.name})",
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = true)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(stringResource(R.string.offline_ai_label))
                                Switch(checked = setup.isAi, onCheckedChange = { on -> viewModel.toggleAi(pid, on) })
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (setup.isAi) {
                                AiDropdown(
                                    selected = setup.difficulty,
                                    onSelect = { diff -> viewModel.setDifficulty(pid, diff) }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            Spacer(modifier = Modifier.weight(1f))
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
private fun AiDropdown(
    modifier: Modifier = Modifier,
    selected: AiDifficulty,
    onSelect: (AiDifficulty) -> Unit
) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val options = listOf(AiDifficulty.Weak, AiDifficulty.Greedy, AiDifficulty.Smart)
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
        modifier = modifier
    ) {
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
            modifier = Modifier
                .menuAnchor()
                .widthIn(min = OfflineConfigAiDropdownMinWidth)
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
private fun ColorDropdown(
    modifier: Modifier = Modifier,
    options: List<ColorOption>,
    selected: Color,
    onSelect: (Color) -> Unit
) {
    val expanded = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    val selectedOption = options.firstOrNull { it.color == selected }
    val labelRes = selectedOption?.labelRes
        ?: options.firstOrNull()?.labelRes
        ?: R.string.offline_color_label
    val label = stringResource(labelRes)
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text(stringResource(R.string.offline_color_label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier
                .menuAnchor()
                .widthIn(min = OfflineConfigColorDropdownMinWidth)
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { option ->
                val name = stringResource(option.labelRes)
                val contentAlpha = if (option.isAvailable) 1f else 0.6f
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.alpha(contentAlpha),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(option.color)
                            )
                            Text(
                                text = name,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    },
                    enabled = true,
                    onClick = {
                        onSelect(option.color)
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

private val OfflineConfigAiDropdownMinWidth = 168.dp
private val OfflineConfigColorDropdownMinWidth = 140.dp
