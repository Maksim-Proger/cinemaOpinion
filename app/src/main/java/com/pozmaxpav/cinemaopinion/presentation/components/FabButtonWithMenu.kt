package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun FabButtonWithMenu(
    imageIcon: ImageVector,
    contentDescription: String,
    textFloatingButton: String,
    content: @Composable () -> Unit,
    onButtonClick: () -> Unit = {}, // TODO: Что это за запись такая?,
    expanded: Boolean
) {
    var menuExpanded by remember { mutableStateOf(false) }

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
            modifier = Modifier
                .width(200.dp)
                .background(MaterialTheme.colorScheme.secondary),
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            offset = DpOffset(x = 0.dp, y = (-16).dp) // Смещение раскрывающегося меню вверх
        ) {
            content()
        }
    }
}