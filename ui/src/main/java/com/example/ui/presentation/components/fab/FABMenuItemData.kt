package com.example.ui.presentation.components.fab

import androidx.compose.runtime.Composable

data class FABMenuItemData(
    val text: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit
)
