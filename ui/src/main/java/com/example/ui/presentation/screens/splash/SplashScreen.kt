package com.example.ui.presentation.screens.splash

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.core.utils.haptic.LocalHapticManager
import com.example.ui.presentation.theme.SplashGradientBottom
import com.example.ui.presentation.theme.SplashGradientMiddle
import com.example.ui.presentation.theme.SplashGradientTop
import com.example.ui.presentation.theme.splashFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ui.R

@Composable
fun SplashScreen(
    isContentReady: Boolean,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticManager = LocalHapticManager.current
    val activity = LocalActivity.current as? ComponentActivity

    val transition = rememberInfiniteTransition(label = "splashGradient")
    val shimmer = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    val titleAlpha = remember { Animatable(0f) }
    val titleSpacing = remember { Animatable(24f) }
    var minimumShown by remember { mutableStateOf(false) }

    SideEffect {
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color.Transparent.toArgb())
        )
    }

    LaunchedEffect(Unit) {
        delay(120)
        hapticManager.playLaunchPulse()
        launch { titleAlpha.animateTo(1f, tween(900, easing = LinearOutSlowInEasing)) }
        titleSpacing.animateTo(10f, tween (1100, easing = FastOutSlowInEasing))
        delay(400)
        minimumShown = true
    }

    LaunchedEffect(minimumShown, isContentReady) {
        if (minimumShown && isContentReady) onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val t = shimmer.value
                drawRect(
                    Brush.linearGradient(
                        colors = listOf(
                            SplashGradientTop,
                            SplashGradientMiddle,
                            SplashGradientBottom
                        ),
                        start = Offset(size.width * (0.5f - t * 0.45f), 0f),
                        end = Offset(
                            size.width * (0.5f + t * 0.45f),
                            size.height * (0.9f + t * 0.25f)
                        )
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.splash_app_title),
            style = MaterialTheme.typography.displayLarge.copy(
                fontFamily = splashFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 30.sp,
                letterSpacing = titleSpacing.value.sp
            ),
            color = Color.White,
            modifier = Modifier.graphicsLayer { alpha = titleAlpha.value }
        )
    }
}
