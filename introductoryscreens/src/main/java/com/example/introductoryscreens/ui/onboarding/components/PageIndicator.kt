package com.example.introductoryscreens.ui.onboarding.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.introductoryscreens.ui.theme.Blue
import com.example.introductoryscreens.ui.theme.BlueGray
import com.example.introductoryscreens.util.Dimens.IndicatorSize

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier, // Модификатор для настройки внешнего вида и поведения компонента
    pageSize: Int, // Общее количество страниц
    selectedPage: Int, // Индекс текущей выбранной страницы
    selectedColor: Color = Blue, // Цвет индикатора для выбранной страницы
    unselectedColor: Color = BlueGray // Цвет индикаторов для невыбранных страниц
) {
    // Горизонтальный контейнер для индикаторов страниц
    Row(
        modifier = modifier, // Применяем модификатор
        horizontalArrangement = Arrangement.SpaceBetween // Распределяем элементы с равными промежутками
    ) {
        // Создаем индикаторы для каждой страницы
        repeat(pageSize) { page ->
            // Создаем круглый индикатор для страницы
            Box(
                modifier = Modifier
                    .size(IndicatorSize) // Устанавливаем размер индикатора
                    .clip(CircleShape) // Задаем форму индикатора (круг)
                    .background(
                        color = if (page == selectedPage) selectedColor else unselectedColor
                    )
            )
        }
    }
}
