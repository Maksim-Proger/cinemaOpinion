package com.example.ui.presentation.components.fab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FABMenu(
    imageIcon: ImageVector,
    contentDescription: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit = {},
    onButtonClick: () -> Unit = {},
    items: List<FABMenuItemData> = emptyList()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButtonMenu(
            expanded = expanded,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .animateFloatingActionButton(visible = true, alignment = Alignment.BottomEnd),
            button = {
                ToggleFloatingActionButton(
                    checked = expanded,
                    onCheckedChange = { onButtonClick() },
                    containerColor = ToggleFloatingActionButtonDefaults.containerColor(
                        initialColor = MaterialTheme.colorScheme.secondary,
                        finalColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = imageIcon,
                        contentDescription = contentDescription,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        ) {
            items.forEach { itemData ->
                FloatingActionButtonMenuItem(
                    text = itemData.text,
                    icon = itemData.icon,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    onClick = {
                        itemData.onClick()
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}




