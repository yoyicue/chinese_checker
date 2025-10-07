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
        lastAppliedLangNorm = canonicalTag(initialTag)
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
                val currentNorm = canonicalTag(current.toLanguageTags())
                val targetNorm = canonicalTag(target.toLanguageTags())
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
        val first = tag.split(',').firstOrNull() ?: tag
        val conf = Configuration(base.resources.configuration)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val ll = android.os.LocaleList.forLanguageTags(first)
            conf.setLocales(ll)
            base.createConfigurationContext(conf)
        } else {
            conf.setLocale(Locale.forLanguageTag(first))
            base.createConfigurationContext(conf)
        }
    }

    private fun canonicalTag(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        val first = raw.trim().replace('_', '-').split(',').first()
        val lower = first.lowercase()
        return when {
            lower.startsWith("zh-hant") -> "zh-Hant"
            lower == "zh" || lower.startsWith("zh-") -> "zh"
            lower == "en" || lower.startsWith("en-") -> "en"
            lower == "es" || lower.startsWith("es-") -> "es"
            lower == "fr" || lower.startsWith("fr-") -> "fr"
            lower == "de" || lower.startsWith("de-") -> "de"
            lower == "it" || lower.startsWith("it-") -> "it"
            lower == "hi" || lower.startsWith("hi-") -> "hi"
            lower == "bn" || lower.startsWith("bn-") -> "bn"
            lower == "mr" || lower.startsWith("mr-") -> "mr"
            lower == "te" || lower.startsWith("te-") -> "te"
            lower.startsWith("pnb") || lower.startsWith("pa-arab") || lower == "pa" || lower.startsWith("pa-pk") -> "pa-Arab"
            lower == "pt" || lower.startsWith("pt-") -> "pt"
            lower == "ru" || lower.startsWith("ru-") -> "ru"
            lower == "ja" || lower.startsWith("ja-") -> "ja"
            lower == "tr" || lower.startsWith("tr-") -> "tr"
            lower == "ko" || lower.startsWith("ko-") -> "ko"
            lower == "vi" || lower.startsWith("vi-") -> "vi"
            else -> lower.substringBefore('-')
        }
    }
}
