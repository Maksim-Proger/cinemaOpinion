package com.example.ui.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex

private val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
private val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InlineBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    containerColor: Color,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
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
            if (targetState) tween(450, easing = EmphasizedDecelerate)
            else tween(300, easing = EmphasizedAccelerate)
        },
        label = "progress"
    ) { shown -> if (shown) 1f else 0f }

    if (!visible && progress == 0f) return

    Box(Modifier.fillMaxSize().semantics { isTraversalGroup = true }) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = progress }
                .background(scrimColor)
                .pointerInput(visible) { if (visible) detectTapGestures { dismiss() } }
                .semantics(mergeDescendants = true) {
                    traversalIndex = 1f
                    contentDescription = "Закрыть"
                    onClick { dismiss(); true }
                }
        )
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .graphicsLayer { translationY = size.height * (1f - progress) }
                .imePadding()
                .clip(shape)
                .background(containerColor)
                .semantics { traversalIndex = 0f }
        ) { content() }
    }
}
