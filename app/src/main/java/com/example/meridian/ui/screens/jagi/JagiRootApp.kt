package com.example.meridian.ui.screens.jagi

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.meridian.data.jagi.JagiRepo

enum class JagiScreen {
    HOME,
    US,
    PLAY,
    MEMORIES,
    LISTS,
    SETTINGS
}

@Composable
fun JagiRootApp(
    repo: JagiRepo = remember { JagiRepo() },
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(JagiScreen.HOME) }
    var showAnswerSheet by remember { mutableStateOf(false) }
    var showFullscreenArena by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAF5EE))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        if (showFullscreenArena) {
            JagiFullscreenGameArena(
                repo = repo,
                onClose = { showFullscreenArena = false }
            )
        } else {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    if (targetState == JagiScreen.HOME) {
                        (fadeIn(animationSpec = tween(220)) + slideInHorizontally { -it / 4 })
                            .togetherWith(fadeOut(animationSpec = tween(180)) + slideOutHorizontally { it / 4 })
                    } else {
                        (fadeIn(animationSpec = tween(220)) + slideInHorizontally { it / 4 })
                            .togetherWith(fadeOut(animationSpec = tween(180)) + slideOutHorizontally { -it / 4 })
                    }
                },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    JagiScreen.HOME -> {
                        JagiHomeScreen(
                            repo = repo,
                            onOpenUs = { currentScreen = JagiScreen.US },
                            onOpenPlay = { currentScreen = JagiScreen.PLAY },
                            onOpenArena = { showFullscreenArena = true },
                            onOpenMemories = { currentScreen = JagiScreen.MEMORIES },
                            onOpenLists = { currentScreen = JagiScreen.LISTS },
                            onOpenSettings = { currentScreen = JagiScreen.SETTINGS },
                            onAnswerQuestion = { showAnswerSheet = true },
                            onEditProfile = { showProfileDialog = true }
                        )
                    }
                    JagiScreen.US -> {
                        JagiUsScreen(
                            repo = repo,
                            onBack = { currentScreen = JagiScreen.HOME },
                            onOpenMemories = { currentScreen = JagiScreen.MEMORIES },
                            onOpenLists = { currentScreen = JagiScreen.LISTS },
                            onEditProfile = { showProfileDialog = true }
                        )
                    }
                    JagiScreen.PLAY -> {
                        JagiPlayScreen(
                            repo = repo,
                            onBack = { currentScreen = JagiScreen.HOME },
                            onOpenArena = { showFullscreenArena = true },
                            onAnswerQuestion = { showAnswerSheet = true }
                        )
                    }
                    JagiScreen.MEMORIES -> {
                        JagiMemoriesScreen(
                            repo = repo,
                            onBack = { currentScreen = JagiScreen.HOME }
                        )
                    }
                    JagiScreen.LISTS -> {
                        JagiListsScreen(
                            repo = repo,
                            onBack = { currentScreen = JagiScreen.HOME }
                        )
                    }
                    JagiScreen.SETTINGS -> {
                        JagiSettingsScreen(
                            repo = repo,
                            onBack = { currentScreen = JagiScreen.HOME },
                            onOpenUsSettings = { currentScreen = JagiScreen.US },
                            onEditProfile = { showProfileDialog = true }
                        )
                    }
                }
            }
        }

        // Answer Sheet
        if (showAnswerSheet) {
            JagiAnswerSheet(
                repo = repo,
                onDismiss = { showAnswerSheet = false }
            )
        }

        // Profile & Couple Customizer Dialog
        if (showProfileDialog) {
            JagiProfileEditDialog(
                repo = repo,
                onDismiss = { showProfileDialog = false }
            )
        }
    }
}
