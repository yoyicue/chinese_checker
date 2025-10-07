package com.yoyicue.chinesechecker

import android.os.Bundle
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.yoyicue.chinesechecker.data.AppContainer
import com.yoyicue.chinesechecker.data.AppSettings
import com.yoyicue.chinesechecker.data.SettingsRepository
import com.yoyicue.chinesechecker.ui.AppProviders
import com.yoyicue.chinesechecker.ui.AppRoot
import com.yoyicue.chinesechecker.ui.theme.ChineseCheckerTheme
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.core.os.LocaleListCompat
import android.util.Log

class MainActivity : ComponentActivity() {
    companion object {
        @Volatile
        private var lastAppliedLangNorm: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply persisted locale as early as possible (before Compose),
        // so resources are loaded under the correct language on first draw.
        val repo = SettingsRepository(applicationContext)
        val initialTag = runBlocking { repo.settings.first().languageTag }
        val initialLocales = if (initialTag.isBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(initialTag)
        }
        AppCompatDelegate.setApplicationLocales(initialLocales)
        lastAppliedLangNorm = initialTag.split(',').firstOrNull()?.substringBefore('-')
        super.onCreate(savedInstanceState)
        val container = AppContainer(applicationContext)
        setContent {
            val settings by container.settingsRepository.settings.collectAsState(initial = AppSettings())
            LaunchedEffect(settings.languageTag) {
                val target = if (settings.languageTag.isBlank()) {
                    LocaleListCompat.getEmptyLocaleList()
                } else {
                    LocaleListCompat.forLanguageTags(settings.languageTag)
                }
                val current = AppCompatDelegate.getApplicationLocales()
                fun norm(tag: String): String =
                    tag.split(',').firstOrNull()?.substringBefore('-') ?: ""
                val currentNorm = norm(current.toLanguageTags())
                val targetNorm = norm(target.toLanguageTags())
                Log.d("Locales", "languageTag='${settings.languageTag}', current='${current.toLanguageTags()}' normCurrent='$currentNorm', target='${target.toLanguageTags()}' normTarget='$targetNorm'")
                // Avoid repeated sets for the same target within the same process
                if (targetNorm != lastAppliedLangNorm && currentNorm != targetNorm) {
                    AppCompatDelegate.setApplicationLocales(target)
                    lastAppliedLangNorm = targetNorm
                    // Trigger a single recreate so all strings reload under new locale
                    this@MainActivity.recreate()
                }
            }
            ChineseCheckerTheme(darkTheme = settings.themeDark) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppProviders(container) {
                        AppRoot()
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        // Ensure base context uses the persisted locale so first draw is correct
        val repo = SettingsRepository(newBase.applicationContext)
        val tag = runBlocking { repo.settings.first().languageTag }
        val ctx = if (tag.isBlank()) newBase else applyLocaleToContext(newBase, tag)
        super.attachBaseContext(ctx)
    }

    private fun applyLocaleToContext(base: Context, tag: String): Context {
        val primary = tag.split(',').firstOrNull()?.substringBefore('-') ?: tag
        val conf = Configuration(base.resources.configuration)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val ll = android.os.LocaleList.forLanguageTags(primary)
            conf.setLocales(ll)
            base.createConfigurationContext(conf)
        } else {
            conf.setLocale(Locale.forLanguageTag(primary))
            base.createConfigurationContext(conf)
        }
    }
}
