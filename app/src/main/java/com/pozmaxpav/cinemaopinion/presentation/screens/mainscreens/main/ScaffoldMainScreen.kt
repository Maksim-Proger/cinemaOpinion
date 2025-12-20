package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.utils.state.LoadingState
import com.example.ui.presentation.components.topappbar.TopAppBarMainScreen
import com.example.ui.presentation.components.fab.FABMenu
import com.example.ui.presentation.components.lottie.AnimationImplementation
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSpecial
import com.pozmaxpav.cinemaopinion.presentation.components.items.fabMenuItems
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
//import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.seasonal.FetchSeasonalMovies
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.SendRequestAdvancedSearch
import com.pozmaxpav.cinemaopinion.utilits.SendSelectedDate
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldMainScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    apiViewModel: ApiViewModel = hiltViewModel()
) {
    val state = rememberMainScreenState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val userId by systemViewModel.userId.collectAsState()
    val loadingState by apiViewModel.loadingState.collectAsState()
    val showDialogEvents by systemViewModel.resultChecking.collectAsState()

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
    }
    LaunchedEffect(state.scrollToTop.value) {
        if (state.scrollToTop.value) {
            state.listState.animateScrollToItem(0)
            state.scrollToTop.value = false
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (
                !state.searchBarActive.value &&
                !state.showDatePicker.value &&
                state.selectedMovie.value == null &&
                state.selectedSeasonalMovie.value == null
            ) {
                TopAppBarMainScreen(
                    title = if (!state.titleTopBarState.value) stringResource(id = R.string.top_app_bar_header_name_all_movies)
                    else stringResource(id = R.string.top_app_bar_header_name_top_list_movies),
                    onSearchButtonClick = { state.searchBarActive.value = true },
                    onAdvancedSearchButtonClick = {
                        state.onAdvancedSearchButtonClick.value = true
                    },
                    onAccountButtonClick = { state.onAccountButtonClick.value = true },
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = {
                        navigateFunction(
                            navController,
                            Route.ListOfChangesScreen.route
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            if (
                !state.searchBarActive.value &&
                !state.onAdvancedSearchButtonClick.value &&
                state.selectedMovie.value == null &&
                state.selectedSeasonalMovie.value == null &&
                !state.showDatePicker.value &&
                !state.locationShowPageAppDescription.value
            ) {
                FABMenu(
                    imageIcon = if (state.isScrolling) Icons.Default.ArrowUpward else Icons.Default.Settings,
                    contentDescription = stringResource(R.string.description_icon_fab_button_with_menu),
                    expanded = state.menuExpanded.value,
                    onExpandedChange = { state.menuExpanded.value = it },
                    onButtonClick = {
                        if (state.isScrolling) state.scrollToTop.value = true
                        else state.menuExpanded.value = !state.menuExpanded.value
                    },
                    items = fabMenuItems(
                        isScrolling = state.isScrolling,
                        titleTopBarState = state.titleTopBarState.value,
                        onFilterToggle = {
                            state.isTopMoviesSelected.value = !state.isTopMoviesSelected.value
                            state.titleTopBarState.value = !state.titleTopBarState.value
                            state.searchCompleted.value = false
                        },
                        onDatePickerToggle = {
                            state.showDatePicker.value = !state.showDatePicker.value
                        },
                        onNavigateToNews = {
                            navigateFunction(navController, Route.MediaNewsScreen.route)
                        }
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            SendSelectedDate(state, apiViewModel)
            SendRequestAdvancedSearch(state, apiViewModel)

            when {
                state.selectedMovie.value != null -> {
                    state.selectedMovie.value?.let {
                        DetailsCardFilm(
                            movie = it,
                            userId = userId,
                            onCloseButton = { state.selectedMovie.value = null },
                            padding = innerPadding,
                            navController = navController
                        )
                    }
                    AdaptiveBackHandler { state.selectedMovie.value = null }
                }

                state.selectedSeasonalMovie.value != null -> {
                    state.selectedSeasonalMovie.value?.let {
                        DetailsCardSpecial(
                            movie = it,
                            userId = userId,
                            onCloseButton = { state.selectedSeasonalMovie.value = null },
                            padding = innerPadding
                        )
                    }
                    AdaptiveBackHandler { state.selectedSeasonalMovie.value = null }
                }

                else -> {

                    when (loadingState) {
                        is LoadingState.Loading -> AnimationImplementation(innerPadding)
                        is LoadingState.Success -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
//                                FetchSeasonalMovies(
//                                    isScrolling = state.isScrolling,
//                                    viewModel = movieViewModel,
//                                    selectedMovie = { movie ->
//                                        state.selectedSeasonalMovie.value = movie
//                                    }
//                                )
                                FetchMovies(
                                    state = state,
                                    apiViewModel = apiViewModel,
                                    selectedMovie = { movie ->
                                        state.selectedMovie.value = movie
                                    }
                                )
                            }
                        }

                        is LoadingState.Error -> {
                            // TODO: Дописать реализацию поведения во время ошибки
                        }
                    }
                }
            }

            DatePickerOverlay(state)
        }
    }

    SearchBarOverlay(state, apiViewModel)
    PreviewOverlay(state, showDialogEvents)
    PageDescriptionOverlay(state, systemViewModel)
    SearchFilterScreenOverlay(state)
    AccountScreenOverlay(userId, state, navController)

}

