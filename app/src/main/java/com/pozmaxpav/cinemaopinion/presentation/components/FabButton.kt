package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.DpOffset
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
        text = {
            Text(
                text = textFloatingButton,
                style = MaterialTheme.typography.bodyLarge)
        },
        expanded = expanded,
        containerColor = colorResource(id = R.color.color_containerColor_fab),
        contentColor = colorResource(id = R.color.color_contentColor_fab)
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
            modifier = Modifier
                .width(200.dp)
                .background(colorResource(R.color.color_background_dropdown_menu)),
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            offset = DpOffset(x = 0.dp, y = (-16).dp) // Смещение раскрывающегося меню вверх
        ) {
            content()
        }
    }
}


