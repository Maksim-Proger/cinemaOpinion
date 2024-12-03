package com.pozmaxpav.cinemaopinion.presentation.components.ratingbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    score: Long?,
    maxScore: Long?,
    numStars: Int,
    starSize: Dp = 48.dp,
){
    // Обработка нулевых значений для score и maxScore
    val safeScore = score ?: 0L
    val safeMaxScore = maxScore ?: 1L // Избегаем деления на ноль

    // Вычисляем текущее значение в звездах
    val fraction = safeScore.toFloat() / safeMaxScore
    val starsFilled = fraction * numStars

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(16.dp)
    ) {
        for (i in 1..numStars) {
            // Расчет частично заполненной звезды
            val starFraction = when {
                i <= starsFilled.toInt() -> 1f // Полностью закрашенная звезда
                (i - 1) < starsFilled -> starsFilled - (i - 1) // Частично закрашенная звезда
                else -> 0f // Пустая звезда
            }

            val starColor = when {
                starFraction == 1f -> Color.Yellow
                starFraction > 0f -> Color.Yellow.copy(alpha = 0.5f) // Полу-заполненная звезда
                else -> Color.Gray
            }

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = starColor
            )
        }
    }
}