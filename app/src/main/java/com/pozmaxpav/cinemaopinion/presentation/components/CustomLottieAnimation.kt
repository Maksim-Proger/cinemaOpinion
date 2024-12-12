package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CustomLottieAnimation(
    modifier: Modifier = Modifier,
    nameFile: String,
    contentScale: ContentScale? = null
) {
    val compositionResult = rememberLottieComposition(LottieCompositionSpec.Asset(nameFile))
    val composition = compositionResult.value

    val animationState = animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    if (composition != null) {
        LottieAnimation(
            composition = composition,
            progress = { animationState.progress },
            modifier = modifier,
            contentScale = contentScale ?: ContentScale.Fit
        )
    } else {
        Text("Loading animation...")
    }
}