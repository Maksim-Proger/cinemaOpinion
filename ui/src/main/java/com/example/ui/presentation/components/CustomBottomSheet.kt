package com.example.ui.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(content: @Composable () -> Unit) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = null,
        sheetGesturesEnabled = false
    ) {
        content()
    }


//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true,
//        confirmValueChange = { newValue ->
//            newValue != SheetValue.Hidden
//        }
//    )
//
//    ModalBottomSheet(
//        sheetState = sheetState,
//        onDismissRequest = onClose,
//        shape = RoundedCornerShape(16.dp),
//        dragHandle = null,
//        containerColor = MaterialTheme.colorScheme.background
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(fraction)
//        ) {
//            content()
//        }
//    }
}
