package com.example.core.utils.haptic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalHapticManager = staticCompositionLocalOf<HapticManager> {
    error("HapticManager не предоставлен!")
}

@Composable
fun HapticManagerProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val hapticManager = remember { HapticManager(context) }

    CompositionLocalProvider(LocalHapticManager provides hapticManager) {
        content()
    }
}
