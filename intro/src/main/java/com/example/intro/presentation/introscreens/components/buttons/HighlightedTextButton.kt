package com.example.intro.presentation.introscreens.components.buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.presentation.theme.IntroButtonColor
import com.example.ui.presentation.theme.IntroTextButtonColor
import com.example.ui.presentation.theme.christmasFont

@Composable
fun HighlightedTextButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = IntroButtonColor,
            contentColor = IntroTextButtonColor
        ),
        shape = RoundedCornerShape(size = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontFamily = christmasFont
            )
        )
    }
}