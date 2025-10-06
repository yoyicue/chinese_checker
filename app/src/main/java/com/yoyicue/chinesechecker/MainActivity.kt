package com.yoyicue.chinesechecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.yoyicue.chinesechecker.data.AppContainer
import com.yoyicue.chinesechecker.data.AppSettings
import com.yoyicue.chinesechecker.ui.AppProviders
import com.yoyicue.chinesechecker.ui.AppRoot
import com.yoyicue.chinesechecker.ui.theme.ChineseCheckerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = AppContainer(applicationContext)
        setContent {
            val settings by container.settingsRepository.settings.collectAsState(initial = AppSettings())
            ChineseCheckerTheme(darkTheme = settings.themeDark) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppProviders(container) {
                        AppRoot()
                    }
                }
            }
        }
    }
}
