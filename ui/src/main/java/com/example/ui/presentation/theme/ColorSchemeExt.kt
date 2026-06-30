package com.example.ui.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val ColorScheme.cardAccent: Color
    get() = if (background.luminance() < 0.5f) Petrol else Sand45

val ColorScheme.onCardAccent: Color
    get() = Sand85
