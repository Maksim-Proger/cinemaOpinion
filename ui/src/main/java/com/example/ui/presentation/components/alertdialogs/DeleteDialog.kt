package com.example.ui.presentation.components.alertdialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.R

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
                style = MaterialTheme.typography.bodyLarge
            )
        },
        text = {
            Text(
                text = "Вы действительно хотите удалить запись: $entryTitle",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            CustomTextButton(
                textButton = stringResource(R.string.delete_entry),
                modifier = Modifier,
                onClickButton = confirmButtonClick
            )
        },
        dismissButton = {
            TextButton(onClick = dismissButtonClick) {
                Text(
                    text = "Отмена",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}
