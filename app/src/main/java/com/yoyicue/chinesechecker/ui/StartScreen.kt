package com.yoyicue.chinesechecker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yoyicue.chinesechecker.BuildConfig

@Composable
fun StartScreen(
    hasContinue: Boolean = false,
    onContinueGame: () -> Unit = {},
    onStartOffline: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenHowToPlay: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("中国跳棋", style = MaterialTheme.typography.headlineLarge)
        val versionName = BuildConfig.VERSION_NAME
        val normalizedVersion = versionName.removePrefix("v").removePrefix("V")
        Text(
            text = "版本 v$normalizedVersion",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )

        if (hasContinue) {
            Button(modifier = Modifier.padding(top = 24.dp), onClick = onContinueGame) { Text("继续对局") }
        }
        Button(modifier = Modifier.padding(top = 8.dp), onClick = onStartOffline) { Text("离线对战") }
        Button(modifier = Modifier.padding(top = 8.dp), onClick = onOpenSettings) { Text("设置") }
        Button(modifier = Modifier.padding(top = 8.dp), onClick = onOpenProfile) { Text("资料") }
        Button(modifier = Modifier.padding(top = 8.dp), onClick = onOpenHowToPlay) { Text("教学") }
    }
}
