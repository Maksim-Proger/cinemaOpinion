package com.example.ui.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.height(100.dp),
        strokeWidth = 5.dp
    )
}

