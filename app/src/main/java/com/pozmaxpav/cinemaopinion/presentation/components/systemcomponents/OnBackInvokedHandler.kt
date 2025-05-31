package com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents

import android.app.Activity
import android.os.Build
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OnBackInvokedHandler(enabled: Boolean = true, onBackInvoked: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity
    val dispatcher = activity?.onBackInvokedDispatcher

    val callback = remember {
        OnBackInvokedCallback {
            onBackInvoked()
        }
    }

    DisposableEffect(dispatcher, enabled) {
        if (enabled && dispatcher != null) {
            dispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                callback
            )
        }

        onDispose {
            dispatcher?.unregisterOnBackInvokedCallback(callback)
        }
    }
}

