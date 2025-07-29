package com.pozmaxpav.cinemaopinion.presentation.components.fab

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FabButton(
    imageIcon: ImageVector,
    contentDescription: String,
    textFloatingButton: String,
    onButtonClick: () -> Unit,
    expanded: Boolean
) {
    ExtendedFloatingActionButton(
        onClick = onButtonClick,
        icon = {
            Icon(
                imageVector = imageIcon,
                contentDescription = contentDescription,
                modifier = Modifier.size(20.dp)
            )
        },
        text = {
            Text(
                text = textFloatingButton,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        expanded = expanded,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    )
}

