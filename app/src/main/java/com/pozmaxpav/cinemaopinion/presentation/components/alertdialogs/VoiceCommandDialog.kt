package com.pozmaxpav.cinemaopinion.presentation.components.alertdialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.ui.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel

@Composable
fun VoiceCommandDialog(
    updated: DomainSeriesControlModel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = updated.title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        text = {
            val description = if (updated.noSeasons) {
                stringResource(R.string.voice_command_result_series_only, updated.series)
            } else {
                stringResource(R.string.voice_command_result, updated.season, updated.series)
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            CustomTextButton(
                textButton = stringResource(R.string.button_save),
                modifier = Modifier,
                onClickButton = onConfirm
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.button_cancel),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}
