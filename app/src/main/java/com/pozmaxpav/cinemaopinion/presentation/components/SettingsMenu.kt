package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun SettingsMenu(
    // Передаем элементы меню как один блок кода и функцию для закрытия меню
    content: @Composable (closeMenu: () -> Unit) -> Unit
) {
    var menuOpeningStatus by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        IconButton(onClick = { menuOpeningStatus = true }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Icon MoreVert"
            )
        }

        DropdownMenu(
            expanded = menuOpeningStatus,
            onDismissRequest = { menuOpeningStatus = false },
            offset = DpOffset(x = 0.dp, y = 10.dp) // Смещение раскрывающегося меню вниз
        ) {
            content { menuOpeningStatus = false } // TODO: Что это за запись такая?
        }
    }
}


@Composable
fun MyDropdownMenuItem(
    onAction: () -> Unit,
    title: String,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall)
        },
        leadingIcon = leadingIcon,
        onClick = { onAction() }
    )
}

