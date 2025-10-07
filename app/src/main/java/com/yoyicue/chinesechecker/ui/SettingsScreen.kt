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
import androidx.compose.ui.res.stringResource
import com.yoyicue.chinesechecker.R
// Locale changes are applied centrally in MainActivity based on DataStore

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
        Row { TextButton(onClick = onBack) { Text(stringResource(R.string.common_back)) } }
        Text(
            text = stringResource(R.string.settings_title),
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
            // Language comes first
            LanguageDropdown(
                selected = s.languageTag,
                onSelect = viewModel::setLanguage
            )

            val volumePercent = (s.soundsVolume * 100).toInt()
            Text(
                stringResource(R.string.settings_sounds, volumePercent),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
            Slider(
                value = s.soundsVolume,
                onValueChange = { v -> viewModel.setSoundsVolume(v) },
                modifier = Modifier.fillMaxWidth()
            )

            SettingSwitchRow(
                title = stringResource(R.string.settings_vibration),
                checked = s.haptics,
                onCheckedChange = { on ->
                    if (on) doHaptic(HapticKind.Success)
                    viewModel.setHaptics(on)
                }
            )

            SettingSwitchRow(
                title = stringResource(R.string.settings_dark_theme),
                checked = s.themeDark,
                onCheckedChange = viewModel::setThemeDark
            )

            SettingSwitchRow(
                title = stringResource(R.string.settings_fast_mode),
                description = stringResource(R.string.settings_fast_mode_desc),
                checked = s.fastGame,
                onCheckedChange = viewModel::setFastGame
            )

            SettingSwitchRow(
                title = stringResource(R.string.settings_long_jump),
                description = stringResource(R.string.settings_long_jump_desc),
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
            Text(stringResource(R.string.settings_ai_section), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            AiDefaultDifficultyDropdown(
                selected = s.aiDifficulty,
                onSelect = viewModel::setAiDifficulty
            )
            SettingSwitchRow(
                title = stringResource(R.string.settings_ai_long_jump),
                checked = s.aiLongJumps,
                onCheckedChange = viewModel::setAiLongJumps
            )

            val debugVisible = debugUnlocked || s.debugOverlay
            if (debugVisible) {
                Spacer(Modifier.height(24.dp))
                SettingSwitchRow(
                    title = stringResource(R.string.settings_debug_toggle),
                    description = stringResource(R.string.settings_debug_desc),
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
private fun LanguageDropdown(selected: String, onSelect: (String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val options = listOf(
        "" to R.string.language_auto,
        "en" to R.string.language_english,
        "zh" to R.string.language_chinese,
        "es" to R.string.language_spanish
    )
    val labelRes = options.firstOrNull { it.first == selected }?.second ?: R.string.language_auto
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = stringResource(labelRes),
            onValueChange = {},
            label = { Text(stringResource(R.string.settings_language)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (tag, resId) ->
                DropdownMenuItem(text = { Text(stringResource(resId)) }, onClick = {
                    onSelect(tag)
                    expanded.value = false
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AiDefaultDifficultyDropdown(selected: Int, onSelect: (Int) -> Unit) {
    val options = listOf(0, 1, 2)
    val expanded = remember { mutableStateOf(false) }
    val label = when (selected) {
        0 -> stringResource(R.string.difficulty_easy)
        2 -> stringResource(R.string.difficulty_hard)
        else -> stringResource(R.string.difficulty_medium)
    }
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = label,
            onValueChange = {},
            label = { Text(stringResource(R.string.settings_ai_default_difficulty)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { code ->
                val text = when (code) {
                    0 -> stringResource(R.string.difficulty_easy)
                    2 -> stringResource(R.string.difficulty_hard)
                    else -> stringResource(R.string.difficulty_medium)
                }
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
        val valueText = stringResource(R.string.settings_turn_duration_option, selected)
        OutlinedTextField(
            readOnly = true,
            value = valueText,
            onValueChange = {},
            label = { Text(stringResource(R.string.settings_turn_duration)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { sec ->
                val optionLabel = stringResource(R.string.settings_turn_duration_option, sec)
                DropdownMenuItem(text = { Text(optionLabel) }, onClick = {
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
    val options = listOf(0 to R.string.settings_timeout_skip, 1 to R.string.settings_timeout_auto)
    val expanded = remember { mutableStateOf(false) }
    val labelRes = options.firstOrNull { it.first == selected }?.second ?: options.first().second
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = stringResource(labelRes),
            onValueChange = {},
            label = { Text(stringResource(R.string.settings_timeout_behavior)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
            options.forEach { (code, resId) ->
                val textValue = stringResource(resId)
                DropdownMenuItem(text = { Text(textValue) }, onClick = {
                    onSelect(code)
                    expanded.value = false
                })
            }
        }
    }
}
