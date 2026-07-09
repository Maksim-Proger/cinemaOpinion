package com.example.ui.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
private val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

@Composable
fun InlineBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    containerColor: Color,
    scrimColor: Color = Color.Black.copy(alpha = 0.32f),
    cornerRadius: Dp = 28.dp,
    content: @Composable () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    fun dismiss() {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
        onDismissRequest()
    }

    BackHandler(enabled = visible) { dismiss() }

    val transition = updateTransition(targetState = visible, label = "InlineBottomSheet")
    val progress by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 450, easing = EmphasizedDecelerate)
            } else {
                tween(durationMillis = 300, easing = EmphasizedAccelerate)
            }
        },
        label = "progress"
    ) { shown -> if (shown) 1f else 0f }

    if (!visible && progress == 0f) return

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = progress }
                .background(scrimColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = visible
                ) { dismiss() }
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .graphicsLayer { translationY = size.height * (1f - progress) }
                .imePadding()
                .clip(RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius))
                .background(containerColor)
        ) {
            content()
        }
    }
}
