package com.example.ui.presentation.components.dropmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.ui.R

@Composable
fun SettingsMenu(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    containerColor: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable (closeMenu: () -> Unit) -> Unit
) {
    var menuOpeningStatus by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.Center)
    ) {
        Icon(
            modifier = Modifier
                .size(28.dp)
                .clickable { menuOpeningStatus = true },
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.description_icon_dropdown_menu_button),
            tint = tint
        )

        DropdownMenu(
            expanded = menuOpeningStatus,
            onDismissRequest = { menuOpeningStatus = false },
            shape = RoundedCornerShape(12.dp),
            containerColor = containerColor,
            offset = DpOffset(x = 0.dp, y = 10.dp)
        ) {
            content { menuOpeningStatus = false }
        }
    }
}
