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
    background = LightBackground, // Основной фон приложения.
    primary = LightColorBackgroundComponents, // Основной цвет для ключевых элементов (кнопки, заголовки).
    onPrimary = LightColorContentComponents, // Цвет текста и компонентов на primary.
    surface = LightColorBackgroundContentApp, // Фон для компонентов (карточек, диалогов).
    onSurface = LightColorBackgroundTextContentApp, // Цвет текста на surface.
    secondary = LightColorInterfaceButtons, // Цвет фона кнопок интерфейса
    onSecondary = LightColorContentInterfaceButtons, // Цвет текста на Fab и DropdownMenu
    tertiary = LightColorBackgroundAccountCard, // Первый фон для карточки аккаунта
    tertiaryContainer = LightColorSecondBackgroundAccountCard, // Второй фон для карточки аккаунта
    onSurfaceVariant = LightColorContentAccountCard, // Цвет текста и кнопок для карточки аккаунта
    outline = Color.DarkGray, // Цвет для границ и обводок.
    outlineVariant = LightColorContentAccountCard, // Вариант цвета для границ.
    error = LightRed
)

private val DarkColorScheme = darkColorScheme(
    background = DarkBackground,
    primary = DarkColorBackgroundComponents,
    onPrimary = DarkColorContentComponents,
    surface = DarkColorBackgroundContentApp,
    onSurface = DarkColorBackgroundTextContentApp,
    secondary = DarkColorInterfaceButtons,
    onSecondary = DarkColorContentInterfaceButtons,
    tertiary = DarkColorBackgroundAccountCard,
    tertiaryContainer = DarkColorSecondBackgroundAccountCard,
    onSurfaceVariant = DarkColorContentAccountCard,
    outline = Color.LightGray,
    outlineVariant = DarkColorContentAccountCard,
    error = DarkRedError
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
            statusBarStyle = SystemBarStyle.auto(
                darkScrim = Color.Transparent.toArgb(),
                lightScrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                darkScrim = Color.Transparent.toArgb(),
                lightScrim = Color.Transparent.toArgb()
            )
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}