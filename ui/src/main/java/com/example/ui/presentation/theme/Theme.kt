package com.example.ui.presentation.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.ui.presentation.viewmodels.ThemeViewModel

private val LightColorScheme = lightColorScheme(
    background = Sand95,
    onBackground = Sand20,
    surface = Sand95,
    onSurface = Sand20,
    surfaceVariant = Sand85,
    onSurfaceVariant = Sand45,
    primary = Sand20,
    onPrimary = Sand95,
    primaryContainer = Sand65,
    onPrimaryContainer = Sand20,
    secondary = Sand45,
    onSecondary = Sand95,
    secondaryContainer = Sand85,
    onSecondaryContainer = Sand20,
    tertiary = Sand65,
    onTertiary = Sand20,
    tertiaryContainer = Sand85,
    onTertiaryContainer = Sand20,
    outline = Sand45,
    outlineVariant = Sand85,
    surfaceDim = Sand85,
    surfaceBright = Sand95,
    surfaceContainerLowest = Sand95,
    surfaceContainerLow = Sand95,
    surfaceContainer = Sand85,
    surfaceContainerHigh = Sand75,
    surfaceContainerHighest = Sand65,
    inverseSurface = Sand20,
    inverseOnSurface = Sand95,
    inversePrimary = Sand65,
)

private val DarkColorScheme = darkColorScheme(
    background = Slate10,
    onBackground = Slate85,
    surface = Slate10,
    onSurface = Slate85,
    surfaceVariant = Slate25,
    onSurfaceVariant = Slate65,
    primary = Slate85,
    onPrimary = Slate10,
    primaryContainer = Slate40,
    onPrimaryContainer = Slate85,
    secondary = Slate65,
    onSecondary = Slate10,
    secondaryContainer = Slate25,
    onSecondaryContainer = Slate85,
    tertiary = Slate40,
    onTertiary = Slate85,
    tertiaryContainer = Slate15,
    onTertiaryContainer = Slate85,
    outline = Slate65,
    outlineVariant = Slate25,
    surfaceDim = Slate10,
    surfaceBright = Slate40,
    surfaceContainerLowest = Slate10,
    surfaceContainerLow = Slate15,
    surfaceContainer = Slate15,
    surfaceContainerHigh = Slate25,
    surfaceContainerHighest = Slate40,
    inverseSurface = Slate85,
    inverseOnSurface = Slate10,
    inversePrimary = Slate25,
)

@Composable
fun AppTheme(
    themeViewModel: ThemeViewModel,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as? ComponentActivity ?: return

    val isDarkThemeActive by themeViewModel.isDarkThemeActive.collectAsState()
    val isSystemThemeActive by themeViewModel.isSystemThemeActive.collectAsState()

    val isSystemInDarkMode = isSystemInDarkTheme()
    val useDarkIcons = if (isSystemThemeActive) !isSystemInDarkMode else !isDarkThemeActive

    val colorScheme = when {
        isSystemThemeActive -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkThemeActive) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDarkThemeActive -> DarkColorScheme
        else -> LightColorScheme
    }

    SideEffect {
        activity.enableEdgeToEdge(
            statusBarStyle = if (useDarkIcons) {
                SystemBarStyle.light(
                    darkScrim = Color.Transparent.toArgb(),
                    scrim = Color.Transparent.toArgb(),
                )
            } else {
                SystemBarStyle.dark(
                    scrim = Color.Transparent.toArgb(),
                )
            },
            navigationBarStyle = if (useDarkIcons) {
                SystemBarStyle.light(
                    darkScrim = Color.Transparent.toArgb(),
                    scrim = Color.Transparent.toArgb(),
                )
            } else {
                SystemBarStyle.dark(
                    scrim = Color.Transparent.toArgb(),
                )
            }
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}