package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.screens.AccountScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    onSearchButtonClick: () -> Unit,
    onAccountButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = { Text(text = stringResource(id = R.string.header_name)) },
        actions = {
            IconButton(onClick = onSearchButtonClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.icon_search)
                )
            }
            IconButton(onClick = onAccountButtonClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = R.string.icon_account)
                )
            }
        }
    )
}
