package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.data.AppSettings
import com.yoyicue.chinesechecker.game.AiDifficulty
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.ui.game.ControllerType
import com.yoyicue.chinesechecker.ui.game.GameConfig
import com.yoyicue.chinesechecker.ui.game.PlayerConfig
import kotlinx.coroutines.launch

@Composable
fun OfflineConfigScreen(
    onBack: () -> Unit,
    onStartGame: () -> Unit
) {
    val container = LocalAppContainer.current
    val settings by container.settingsRepository.settings.collectAsState(initial = AppSettings())
    val scope = rememberCoroutineScope()

    val playerCountOptions = listOf(2, 3, 4, 6)
    val playerCountState = remember { mutableStateOf(2) }

    val defaultColors = mapOf(
        Board.PlayerId.A to Color(0xFFE53935), // red
        Board.PlayerId.B to Color(0xFF1E88E5), // blue
        Board.PlayerId.C to Color(0xFF8E24AA), // purple
        Board.PlayerId.D to Color(0xFF43A047), // green
        Board.PlayerId.E to Color(0xFFFDD835), // yellow
        Board.PlayerId.F to Color(0xFFFF7043)  // orange
    )
    val colorPalette: List<Pair<Color, String>> = listOf(
        Color(0xFFE53935) to "红",
        Color(0xFF1E88E5) to "蓝",
        Color(0xFF8E24AA) to "紫",
        Color(0xFF43A047) to "绿",
        Color(0xFFFDD835) to "黄",
        Color(0xFFFF7043) to "橙",
        Color(0xFF26A69A) to "青"
    )

    data class SetupState(
        val isAI: androidx.compose.runtime.MutableState<Boolean>,
        val difficulty: androidx.compose.runtime.MutableState<AiDifficulty>,
        val color: androidx.compose.runtime.MutableState<Color>
    )

    val setups = remember(settings) {
        mutableStateMapOf<Board.PlayerId, SetupState>().apply {
            val defaultDiff = when (settings.aiDifficulty) {
                0 -> AiDifficulty.Weak
                1 -> AiDifficulty.Greedy
                2 -> AiDifficulty.Smart
                else -> AiDifficulty.Greedy
            }
            Board.PlayerId.values().forEach { pid ->
                put(
                    pid,
                    SetupState(
                        isAI = mutableStateOf(false),
                        difficulty = mutableStateOf(defaultDiff),
                        color = mutableStateOf(defaultColors[pid] ?: Color.Gray)
                    )
                )
            }
        }
    }

    val seats = remember(playerCountState.value) { seatsForCount(playerCountState.value) }

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
                CountDropdown(options = playerCountOptions, selected = playerCountState.value, onSelect = { playerCountState.value = it })
            }

            Column(modifier = Modifier.padding(top = 8.dp)) {
                seats.forEach { (pid, label) ->
                    val st = setups.getValue(pid)
                    // Colors chosen by others
                    val chosenByOthers = seats.filter { it.first != pid }.map { setups.getValue(it.first).color.value }.toSet()
                    val optionsForThis = colorPalette.filter { it.first !in chosenByOthers }
                    // Ensure current selection remains valid; if not, force to first available
                    if (optionsForThis.none { it.first == st.color.value }) {
                        optionsForThis.firstOrNull()?.let { st.color.value = it.first }
                    }
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
                                Switch(checked = st.isAI.value, onCheckedChange = { st.isAI.value = it })
                            }
                            Row(
                                modifier = Modifier.padding(top = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (st.isAI.value) {
                                    AiDropdown(
                                        selected = st.difficulty.value,
                                        onSelect = { st.difficulty.value = it },
                                    )
                                }
                                ColorDropdown(
                                    options = optionsForThis,
                                    selected = st.color.value,
                                    onSelect = { st.color.value = it }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = {
                val players = seats.map { (pid, _) ->
                    val st = setups.getValue(pid)
                    PlayerConfig(
                        playerId = pid,
                        controller = if (st.isAI.value) ControllerType.AI else ControllerType.Human,
                        difficulty = if (st.isAI.value) st.difficulty.value else null,
                        color = st.color.value
                    )
                }
                val config = GameConfig(
                    playerCount = playerCountState.value,
                    players = players
                )
                scope.launch {
                    container.pendingRestore = null
                    container.lastGameConfig = config
                    container.gameRepository.clearSave()
                    onStartGame()
                }
            }
        ) { Text("开始游戏") }
    }
}

private fun seatsForCount(n: Int): List<Pair<Board.PlayerId, String>> = when (n) {
    2 -> listOf(
        Board.PlayerId.A to "上(Top)",
        Board.PlayerId.B to "下(Bottom)"
    )
    3 -> listOf(
        Board.PlayerId.A to "上(Top)",
        Board.PlayerId.B to "左下(SW)",
        Board.PlayerId.C to "右下(SE)"
    )
    4 -> listOf(
        Board.PlayerId.A to "上(Top)",
        Board.PlayerId.B to "下(Bottom)",
        Board.PlayerId.C to "右上(NE)",
        Board.PlayerId.D to "左下(SW)"
    )
    else -> listOf(
        Board.PlayerId.A to "上(Top)",
        Board.PlayerId.C to "右上(NE)",
        Board.PlayerId.F to "右下(SE)",
        Board.PlayerId.B to "下(Bottom)",
        Board.PlayerId.D to "左下(SW)",
        Board.PlayerId.E to "左上(NW)"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountDropdown(options: List<Int>, selected: Int, onSelect: (Int) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
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
    val expanded = remember { mutableStateOf(false) }
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
    val expanded = remember { mutableStateOf(false) }
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
