package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = MeridianApricot,
    onPrimary = MeridianDeepNight,
    primaryContainer = Color(0xFF38231E),
    onPrimaryContainer = MeridianApricotSoft,
    secondary = MeridianDuskBlue,
    onSecondary = Color.White,
    background = MeridianDeepNight,
    onBackground = MeridianTextPrimaryDark,
    surface = MeridianSurfaceDark,
    onSurface = MeridianTextPrimaryDark,
    surfaceVariant = MeridianCardDark,
    onSurfaceVariant = MeridianTextSecondaryDark
)

private val LightColorScheme = lightColorScheme(
    primary = MeridianApricot,
    onPrimary = Color.White,
    primaryContainer = MeridianApricotSoft,
    onPrimaryContainer = Color(0xFF4E2010),
    secondary = MeridianDuskBlue,
    onSecondary = Color.White,
    background = MeridianOffWhite,
    onBackground = MeridianTextPrimaryLight,
    surface = MeridianSurfaceLight,
    onSurface = MeridianTextPrimaryLight,
    surfaceVariant = Color(0xFFF3ECE4),
    onSurfaceVariant = MeridianTextSecondaryLight
)

@Composable
fun MeridianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MeridianTheme(darkTheme = darkTheme, content = content)
}
