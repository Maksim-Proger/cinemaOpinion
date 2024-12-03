package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pozmaxpav.cinemaopinion.utilits.Constants.TEXT_FOR_ALERT_DIALOG

@Composable
fun ShowDialogEvents(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Card (
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Ура!!!! \uD83C\uDF89",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.wrapContentSize().padding(16.dp)
            ) {
               Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.Start
               ) {
                   Text(
                       text = "✨",
                       fontSize = 48.sp
                   )
               }
               Spacer(modifier = Modifier.padding(16.dp))
               Row(
                   modifier = Modifier.fillMaxWidth()
               ) {
                   Text(
                       text = TEXT_FOR_ALERT_DIALOG,
                       style = MaterialTheme.typography.titleMedium,
                       color = MaterialTheme.colorScheme.secondary
                   )
               }
            }

        }
    )
}