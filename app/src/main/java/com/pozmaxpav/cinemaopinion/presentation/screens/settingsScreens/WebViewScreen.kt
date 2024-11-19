package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.WebViewViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    viewModel: WebViewViewModel = hiltViewModel()
) {
    val url by viewModel.url.collectAsState()

    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    })
}

