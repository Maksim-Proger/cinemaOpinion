package com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.runtime.Composable
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun AdaptiveBackHandler(
    enabled: Boolean = true,
    onProgress: (Float) -> Unit = {},
    action: () -> Unit
) {
    PredictiveBackHandler(enabled = enabled) { events ->
        try {
            events.collect { event -> onProgress(event.progress) }
            action()
        } catch (e: CancellationException) {
            onProgress(0f)
        }
    }
}

