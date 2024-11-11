package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FabButton(
    imageIcon: ImageVector,
    contentDescription: String,
    textFloatingButton: String,
    onButtonClick: () -> Unit,
    expanded: Boolean
) {
    ExtendedFloatingActionButton(
        onClick = onButtonClick, // TODO: Надо еще раз почитать чем отличается такая передача от onButtonClick()
        icon = {
            Icon(
                imageVector = imageIcon,
                contentDescription = contentDescription
            )
        },
        text = {
            Text(
                text = textFloatingButton,
                style = MaterialTheme.typography.bodyLarge)
        },
        expanded = expanded,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
}

