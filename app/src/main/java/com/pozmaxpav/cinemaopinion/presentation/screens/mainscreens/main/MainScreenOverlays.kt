package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.intro.presentation.pages.PageDescription
import com.example.intro.presentation.pages.PreviewAlertDialog
import com.example.ui.presentation.components.CustomBoxShowOverlay
import com.example.ui.presentation.components.CustomSearchBar
import com.example.ui.presentation.components.DatePickerFunction
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.AccountScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens.SearchFilterScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel

@Composable
fun SearchBarOverlay(
    state: MainScreenState,
    apiViewModel: ApiViewModel
) {
    if (state.searchBarActive.value) {
        CustomBoxShowOverlay(
            paddingVerticalSecondBox = 50.dp,
            paddingHorizontalSecondBox = 16.dp,
            content = {
                CustomSearchBar(
                    query = state.query.value,
                    onQueryChange = { state.query.value = it },
                    onSearch = { searchQuery ->
                        state.currentPage.intValue = 1
                        apiViewModel.fetchSearchMovies(searchQuery, state.currentPage.intValue)
                        state.saveSearchQuery.value = searchQuery
                        state.searchHistory.add(searchQuery)
                        state.searchCompleted.value = true
                        state.searchBarActive.value = false
                    },
                    active = state.searchBarActive.value,
                    onActiveChange = { isActive -> state.searchBarActive.value = isActive },
                    searchHistory = state.searchHistory
                )
            }
        )
    }
}

@Composable
fun PreviewOverlay(
    state: MainScreenState,
    showDialogEvents: Boolean
) {
    if (!showDialogEvents && !state.locationShowDialogEvents.value) {
        CustomBoxShowOverlay(
            content = {
                PreviewAlertDialog {
                    state.locationShowDialogEvents.value = true
                    state.locationShowPageAppDescription.value = true
                }
            }
        )
    }
}

@Composable
fun PageDescriptionOverlay(
    state: MainScreenState,
    systemViewModel: SystemViewModel
) {
    if (state.locationShowPageAppDescription.value) {
        var flag by remember { mutableStateOf(false) }
        CustomBoxShowOverlay(
            content = {
                PageDescription(
                    onDismiss = {
                        state.locationShowPageAppDescription.value = false
                        flag = true
                    }
                )
            }
        )
        LaunchedEffect(flag) {
            systemViewModel.resetResultChecking()
        }
    }
}

@Composable
fun SearchFilterScreenOverlay(state: MainScreenState) {
    if (state.onAdvancedSearchButtonClick.value) {
        CustomBoxShowOverlay(
            content = {
                SearchFilterScreen(
                    onClickClose = { state.onAdvancedSearchButtonClick.value = false },
                    onSendRequest = { state.sendRequestCompleted.value = true },
                    onSearch = {
                        state.requestBody.value = it
                        state.searchCompleted.value = true
                    }
                )
                AdaptiveBackHandler { state.onAdvancedSearchButtonClick.value = false }
            }
        )
    }
}

@Composable
fun AccountScreenOverlay(
    userId: String,
    state: MainScreenState,
    navController: NavHostController
) {
    if (state.onAccountButtonClick.value) {
        CustomBoxShowOverlay(
            onDismiss = { state.onAccountButtonClick.value = false },
            paddingVerticalSecondBox = 70.dp,
            paddingHorizontalSecondBox = 14.dp,
            content = {
                AccountScreen(
                    navController,
                    userId,
                    onClick = { state.onAccountButtonClick.value = false }
                )
                AdaptiveBackHandler { state.onAccountButtonClick.value = false }
            }
        )
    }
}

@Composable
fun DatePickerOverlay(state: MainScreenState) {
    if (state.showDatePicker.value) {
        DatePickerFunction(
            onCloseDatePicker = {
                state.showDatePicker.value = !state.showDatePicker.value
                state.dateSelectionComplete.value = true
            },
            onDateSelected = { date ->
                state.selectedDate.value = date
            },
        )
        AdaptiveBackHandler { state.showDatePicker.value = false }
    }
}


















