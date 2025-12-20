package com.example.ui.presentation.components.alertdialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    entryTitle: String,
    onDismissRequest: () -> Unit,
    confirmButtonClick: () -> Unit,
    dismissButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Подтверждение удаления",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            Text(
                text = "Вы действительно хотите удалить запись: $entryTitle",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        confirmButton = {
            TextButton(onClick = confirmButtonClick) {
                Text(
                    text = "Удалить",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = dismissButtonClick) {
                Text(
                    text = "Отмена",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    )
}