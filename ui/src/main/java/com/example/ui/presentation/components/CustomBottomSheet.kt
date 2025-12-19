package com.example.ui.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    onCloseRequest: () -> Unit,
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
        onDismissRequest = { closeSheet() }
    ) {
        content { closeSheet() }
    }

}
