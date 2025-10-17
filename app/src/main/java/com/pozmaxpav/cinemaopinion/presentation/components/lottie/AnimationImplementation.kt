package com.pozmaxpav.cinemaopinion.presentation.components.lottie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun AnimationImplementation(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        CustomLottieAnimation(
            nameFile = "loading_animation.lottie",
            modifier = Modifier.scale(0.5f)
        )
    }
}