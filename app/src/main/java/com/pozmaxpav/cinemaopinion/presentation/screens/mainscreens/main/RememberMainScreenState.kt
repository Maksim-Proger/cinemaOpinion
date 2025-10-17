package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest

@Composable
fun rememberMainScreenState(): MainScreenState {
    val listState = rememberLazyListState()
    return remember {
        MainScreenState(
            listState = listState,
            selectedDate = mutableStateOf(null),
            dateSelectionComplete = mutableStateOf(false),
            query = mutableStateOf(""),
            searchBarActive = mutableStateOf(false),
            searchCompleted = mutableStateOf(false),
            searchHistory = mutableStateListOf(),
            requestBody = mutableStateOf(CompositeRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )),
            sendRequestCompleted = mutableStateOf(false),
            showDatePicker = mutableStateOf(false),
            isTopMoviesSelected = mutableStateOf(false),
            onAccountButtonClick = mutableStateOf(false),
            onAdvancedSearchButtonClick = mutableStateOf(false),
            locationShowDialogEvents = mutableStateOf(false),
            locationShowPageAppDescription = mutableStateOf(false),
            scrollToTop = mutableStateOf(false),
            menuExpanded = mutableStateOf(false),
            titleTopBarState = mutableStateOf(false),
            currentPage = mutableIntStateOf(1),
            showPageSwitchingButtons = mutableStateOf(false),
            saveSearchQuery = mutableStateOf(""),
            selectedMovie = mutableStateOf(null),
            selectedSeasonalMovie = mutableStateOf(null)
        )
    }
}