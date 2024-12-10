package com.example.introductoryscreens.ui.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.introductoryscreens.ui.theme.WhiteGray

@Composable
fun OnBoardingScreenButton(
    text: String, // Текст кнопки
    onClick: () -> Unit // Обработчик нажатия на кнопку
) {
    // Создаем кнопку с заданными параметрами
    Button(
        onClick = onClick, // Устанавливаем обработчик нажатия
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue, // Цвет фона кнопки
            contentColor = Color.White // Цвет текста на кнопке
        ),
        shape = RoundedCornerShape(size = 8.dp) // Форма кнопки с закругленными углами
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 22.sp
            )
        )
    }
}

@Composable
fun OnBoardingScreenTextButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 22.sp
            ),
            color = WhiteGray
        )
    }
}
