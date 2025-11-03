package com.example.ui.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsRadioButtons(
    indexSelectedTheme: Int,
    optionsList: List<String>,
    onOptionClick: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(optionsList[indexSelectedTheme]) }

    optionsList.forEach { option ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectable(
                    selected = selectedOption == option,
                    onClick = {
                        selectedOption = option
                        onOptionClick(option)
                    }
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedOption == option,
                onClick = null // обработка не требуется, так как обработчик находится в selectable
            )

            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

