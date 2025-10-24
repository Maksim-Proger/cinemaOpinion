package com.example.intro.presentation.introscreens.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import com.example.ui.presentation.theme.IntroButtonColor

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int, // Общее количество страниц
    selectedPage: Int, // Индекс текущей выбранной страницы
    selectedColor: Color = IntroButtonColor, // Цвет индикатора для выбранной страницы
    unselectedColor: Color = DarkGray // Цвет индикаторов для невыбранных страниц
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Создаем индикаторы для каждой страницы
        repeat(pageSize) { page ->
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (page == selectedPage) selectedColor else unselectedColor
                    )
            )
        }
    }
}
