package com.example.ui.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    onCloseRequest: () -> Unit,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    content: @Composable (onClose: () -> Unit) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()

    fun closeSheet() {
        scope.launch {
            sheetState.hide()
            onCloseRequest()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = null,
        containerColor = containerColor,
        contentWindowInsets = contentWindowInsets,
        onDismissRequest = { closeSheet() }
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        content {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
            closeSheet()
        }
    }

}
