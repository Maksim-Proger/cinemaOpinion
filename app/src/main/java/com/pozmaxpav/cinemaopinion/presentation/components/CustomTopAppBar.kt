package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pozmaxpav.cinemaopinion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onSearchButtonClick: () -> Unit,
    onAdvancedSearchButtonClick: () -> Unit,
    onAccountButtonClick: () -> Unit,
    onAction: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(onClick = onSearchButtonClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.description_icon_search),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = onAdvancedSearchButtonClick) {
                Icon(
                    imageVector = Icons.Default.ManageSearch,
                    contentDescription = stringResource(id = R.string.description_icon_advanced_search),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(
                onClick = { onAction() }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(id = R.string.description_icon_account),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = onAccountButtonClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = R.string.description_icon_account),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
