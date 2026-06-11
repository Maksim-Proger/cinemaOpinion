package com.example.ui.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.R

@OptIn(ExperimentalMaterial3Api::class) // Требуется для Card с onClick
@Composable
fun ExpandedCard(
    title: String,
    description: String,
    animatedAccent: Color, // Добавлен параметр для вашего акцентного цвета из нового дизайна
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    var expandedState by remember { mutableStateOf(false) }

    // Анимация вращения стрелки
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = "arrow_rotation"
    )
    val scrollState = rememberScrollState()

    // Используем OutlinedCard для имитации OutlinedButton, но с возможностью раскрытия
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize( // Анимация изменения размера остается на месте
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(24.dp), // Новый радиус из вашего дизайна
        onClick = { expandedState = !expandedState },
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent, // Прозрачный фон
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, animatedAccent) // Анимированная или статичная рамка
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ЗАГОЛОВОК (выглядит и ведет себя как ваша OutlinedButton)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp) // Высота применяется ТОЛЬКО к закрытой части (как у кнопки)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = animatedAccent
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    modifier = Modifier.weight(1f), // 1f заставит текст занять все свободное место и прижать стрелку вправо
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotationState), // Анимация стрелки
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            // РАСКРЫВАЮЩАЯСЯ ЧАСТЬ
            if (expandedState) {
                Column(
                    modifier = Modifier
                        .heightIn(max = 200.dp) // heightIn лучше, чем фиксированная height(200.dp), если текста мало
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .verticalScroll(scrollState)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = animatedAccent.copy(alpha = 0.5f) // Сделал разделитель в тон рамке
                    )

                    Text(
                        text = description,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }
}

