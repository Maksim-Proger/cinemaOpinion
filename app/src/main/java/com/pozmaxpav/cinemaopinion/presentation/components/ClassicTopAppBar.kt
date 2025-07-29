package com.pozmaxpav.cinemaopinion.presentation.components

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
fun ClassicTopAppBar(
    context: Context,
    titleId: Int = 0,
    titleString: String = "",
    scrollBehavior: TopAppBarScrollBehavior,
    onShowTransitionAction: Boolean = true,
    onTransitionAction: () -> Unit = {}
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            if (titleString.isNotEmpty()) {
                Text(
                    text = titleString,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = context.getString(titleId),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        actions = {
            if (onShowTransitionAction) {
                IconButton(onClick = onTransitionAction) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(id = R.string.description_icon_home_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        )
    )
}