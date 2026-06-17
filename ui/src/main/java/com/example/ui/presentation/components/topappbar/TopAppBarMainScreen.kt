package com.example.ui.presentation.components.topappbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
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
    onAccountButtonClick: () -> Unit,
    onTransitionAction: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge
            )
        },
        actions = {
            IconButton(onClick = onTransitionAction) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.description_icon_notifications_button)
                )
            }
            IconButton(onClick = onAccountButtonClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = R.string.description_icon_account_button)
                )
            }
        }
    )
}
