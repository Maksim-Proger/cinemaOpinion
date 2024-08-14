package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.pozmaxpav.cinemaopinion.R

@Composable
fun FabButton(
    imageIcon: ImageVector,
    contentDescription: String,
    textFloatingButton: String
) {
    ExtendedFloatingActionButton(
        onClick = { /*TODO*/ },
        icon = {
            Icon(
                imageVector = imageIcon,
                contentDescription = contentDescription
            )
        },
        text = { Text(text = textFloatingButton) }
    )
}