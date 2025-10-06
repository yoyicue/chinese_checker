package com.yoyicue.chinesechecker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val container = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            val hasSaveState = remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { hasSaveState.value = container.gameRepository.hasSave() }
            StartScreen(
                hasContinue = hasSaveState.value,
                onContinueGame = {
                    scope.launch {
                        val restored = container.gameRepository.load()
                        if (restored != null) {
                            container.lastGameConfig = restored.config
                            container.pendingRestore = restored
                            navController.navigate("game")
                        }
                    }
                },
                onStartOffline = { navController.navigate("offline") },
                onOpenSettings = { navController.navigate("settings") },
                onOpenProfile = { navController.navigate("profile") },
                onOpenHowToPlay = { navController.navigate("howto") }
            )
        }
        composable("offline") {
            OfflineConfigScreen(
                onBack = { navController.popBackStack() },
                onStartGame = { navController.navigate("game") }
            )
        }
        composable("game") { GameScreen(onBack = { navController.popBackStack() }) }
        composable("settings") { SettingsScreen(onBack = { navController.popBackStack() }) }
        composable("profile") { ProfileScreen(onBack = { navController.popBackStack() }) }
        composable("howto") { HowToPlayScreen(onBack = { navController.popBackStack() }) }
        composable("ai_guide") { AiGuideScreen(onBack = { navController.popBackStack() }) }
    }
}
