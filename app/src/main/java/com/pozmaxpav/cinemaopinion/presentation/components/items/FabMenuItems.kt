package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.example.ui.presentation.components.fab.FABMenuItemData

@Composable
fun fabMenuItems(
    isScrolling: Boolean,
    onDatePickerToggle: () -> Unit
): List<FABMenuItemData> {
    val modifier = Modifier.size(20.dp)
    if (isScrolling) return emptyList()
    val items = mutableListOf<FABMenuItemData>()

    items += FABMenuItemData(
            text = {
                Text(
                    text = stringResource(id = R.string.drop_down_menu_item_select_date),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            icon = {
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.drop_down_menu_item_select_date),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            onClick = onDatePickerToggle
        )

    return items
}