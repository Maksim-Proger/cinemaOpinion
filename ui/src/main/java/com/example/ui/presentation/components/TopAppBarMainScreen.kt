package com.example.ui.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContentPasteSearch
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
import com.example.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMainScreen(
    title: String,
    onSearchButtonClick: () -> Unit,
    onAdvancedSearchButtonClick: () -> Unit,
    onAccountButtonClick: () -> Unit,
    onTransitionAction: () -> Unit,
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
                    contentDescription = stringResource(id = R.string.description_icon_search_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = onAdvancedSearchButtonClick) {
                Icon(
                    imageVector = Icons.Default.ContentPasteSearch,
                    contentDescription = stringResource(id = R.string.description_icon_advanced_search_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = onTransitionAction) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.description_icon_notifications_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = onAccountButtonClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = R.string.description_icon_account_button),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}
