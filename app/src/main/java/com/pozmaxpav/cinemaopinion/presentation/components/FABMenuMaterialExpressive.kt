package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FABMenuMaterialExpressive(
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
                    onCheckedChange = { onButtonClick() }
                ) {
                    Icon(imageIcon, contentDescription = contentDescription)
                }
            }
        ) {
            items.forEach { itemData ->
                FloatingActionButtonMenuItem(
                    text = itemData.text,
                    icon = itemData.icon,
                    onClick = {
                        itemData.onClick()
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

data class FABMenuItemData(
    val text: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit
)


