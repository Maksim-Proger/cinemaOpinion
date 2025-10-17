package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.FABMenuItemData

@Composable
fun fabMenuItems(
    isScrolling: Boolean,
    titleTopBarState: Boolean,
    onFilterToggle: () -> Unit,
    onDatePickerToggle: () -> Unit,
    onNavigateToNews: () -> Unit
): List<FABMenuItemData> {
    if (isScrolling) return emptyList()
    val items = mutableListOf<FABMenuItemData>()

    items += FABMenuItemData(
        text = {
            Text(
                if (!titleTopBarState)
                    stringResource(id = R.string.drop_down_menu_item_premiere_movies)
                else
                    stringResource(id = R.string.drop_down_menu_item_topList_movies)
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_movies),
                contentDescription = stringResource(id = R.string.description_icon_settings),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
        onClick = onFilterToggle
    )

    if (!titleTopBarState) {
        items += FABMenuItemData(
            text = {
                Text(
                    text = stringResource(id = R.string.drop_down_menu_item_select_date)
                )
            },
            icon = {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.drop_down_menu_item_select_date),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            },
            onClick = onDatePickerToggle
        )
    }

    items += FABMenuItemData(
        text = {
            Text(
                text = stringResource(R.string.drop_down_menu_item_nac_to_media_news_screen)
            )
        },
        icon = {
            Icon(
                Icons.Default.Newspaper,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        },
        onClick = onNavigateToNews
    )

    return items
}