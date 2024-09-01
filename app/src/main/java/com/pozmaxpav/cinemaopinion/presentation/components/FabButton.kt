package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R

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
                contentDescription = contentDescription
            )
        },
        text = { Text(text = textFloatingButton) },
        expanded = expanded
    )
}


@Composable
fun FabButtonWithMenu(
    imageIcon: ImageVector,
    contentDescription: String,
    textFloatingButton: String,
    content: @Composable () -> Unit,
    onButtonClick: () -> Unit = {}, // TODO: Что это за запись такая?,
    expanded: Boolean
) {
    var menuExpanded by remember { mutableStateOf(false) } // Состояние для отображения DropdownMenu

    Box {
        FabButton(
            imageIcon = imageIcon,
            contentDescription = contentDescription,
            textFloatingButton = textFloatingButton,
            onButtonClick = {
                onButtonClick() // TODO: Что это за запись такая?
                menuExpanded = true
            },
            expanded = expanded
        )

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier.width(200.dp)
        ) {
            content()
        }
    }
}


