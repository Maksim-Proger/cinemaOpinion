package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// region ─── Цвета ────────────────────────────────────────────────────────────────────
private val BgDark = Color(0xFF1C1209)
private val CardDark = Color(0xFF2A1E0F)
private val GlassWhite = Color(0x33FFFFFF)
private val GlassBorder = Color(0x55FFFFFF)
private val TextWhite = Color(0xFFFFFFFF)
private val TextSubtle = Color(0xFFB8A08A)
private val TextMuted = Color(0xFF7A6A55)
// endregion


@Composable
fun NewAccountScreen(

) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

        }
    }

}






















