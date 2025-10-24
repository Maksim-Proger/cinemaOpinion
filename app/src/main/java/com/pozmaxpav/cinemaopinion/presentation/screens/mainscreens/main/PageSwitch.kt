package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel

@Composable
fun PageSwitch(
    state: MainScreenState,
    canGoBack: Boolean,
    apiViewModel: ApiViewModel,
    canGoForward: Boolean
) {

    LaunchedEffect(Unit) {
        snapshotFlow { state.listState.layoutInfo }
            .collect { layoutInfo ->
                val totalItems = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                state.showPageSwitchingButtons.value = lastVisibleItemIndex >= totalItems - 1
            }
    }

    if (state.showPageSwitchingButtons.value) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 16.dp)
        ) {
            if (canGoBack) {
                IconButton(
                    onClick = {
                        state.currentPage.intValue--
                        if (state.isTopMoviesSelected.value) {
                            apiViewModel.fetchTopListMovies(
                                page = state.currentPage.intValue
                            )
                        } else if (state.searchCompleted.value) {
                            apiViewModel.fetchSearchMovies(
                                keyword = state.saveSearchQuery.value,
                                page = state.currentPage.intValue
                            )
                        }
                        state.scrollToTop.value = true
                    },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_previous_page),
                        contentDescription = stringResource(id = R.string.description_icon_previous_page_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (canGoForward) {
                IconButton(
                    onClick = {
                        state.currentPage.intValue++
                        if (state.isTopMoviesSelected.value) {
                            apiViewModel.fetchTopListMovies(
                                page = state.currentPage.intValue
                            )
                        } else if (state.searchCompleted.value) {
                            apiViewModel.fetchSearchMovies(
                                keyword = state.saveSearchQuery.value,
                                page = state.currentPage.intValue
                            )
                        }
                        state.scrollToTop.value = true
                    },
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_next_page),
                        contentDescription = stringResource(id = R.string.description_icon_next_page_button),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}