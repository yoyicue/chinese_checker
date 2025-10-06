package com.yoyicue.chinesechecker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.yoyicue.chinesechecker.data.AppContainer

val LocalAppContainer = staticCompositionLocalOf<AppContainer> { error("AppContainer not provided") }

@Composable
fun AppProviders(container: AppContainer, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppContainer provides container, content = content)
}

