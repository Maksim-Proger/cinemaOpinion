package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.utils.state.LoadingState
import com.example.ui.presentation.components.fab.FABMenu
import com.example.ui.presentation.components.lottie.AnimationImplementation
import com.example.ui.presentation.components.topappbar.TopAppBarMainScreen
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSpecial
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.NewDesignMovieDetailScreen
import com.pozmaxpav.cinemaopinion.presentation.components.items.fabMenuItems
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.SendRequestAdvancedSearch
import com.pozmaxpav.cinemaopinion.utilities.SendSelectedDate
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction
import com.pozmaxpav.cinemaopinion.utilities.showToast2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldMainScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    apiViewModel: ApiViewModel = hiltViewModel(),
    onExit: () -> Unit = {}
) {

    // region Переменные
    var backPressedTime by remember { mutableLongStateOf(0L) }
    val backPreventTime = 2000L

    val state = rememberMainScreenState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isScrolling by remember {
        derivedStateOf {
            scrollBehavior.state.heightOffset < 0f ||
                state.listState.firstVisibleItemIndex > 0 ||
                state.listState.firstVisibleItemScrollOffset > 0
        }
    }

    val userId by systemViewModel.userId.collectAsState()
    val loadingState by apiViewModel.loadingState.collectAsState()
    val showDialogEvents by systemViewModel.resultChecking.collectAsState()
    // endregion

    // region Push Notification

    val context = LocalContext.current
    val notViewModel: NotificationViewModel = hiltViewModel()
    val status by notViewModel.statusReg.collectAsState()
    val deviceId = remember { Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) }

    LaunchedEffect(userId, status) {
        if (userId != "Unknown" && !status) {
            // Отправляем pushToken и deviceId в модуль backend для дальнейшей отправки на сервер
            notViewModel.registerDevice(userId, deviceId)
        }
    }

    // endregion

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
        notViewModel.getStatus()
    }
    LaunchedEffect(state.searchCompleted.value) {
        if (state.searchCompleted.value) {
            navController.navigate(Route.ApiListScreen.navigate("search"))
            state.searchCompleted.value = false
        }
    }
    LaunchedEffect(state.scrollToTop.value) {
        if (state.scrollToTop.value) {
            state.listState.animateScrollToItem(0)
            scrollBehavior.state.heightOffset = 0f
            scrollBehavior.state.contentOffset = 0f
            state.scrollToTop.value = false
        }
    }

    BackHandler(enabled = true) {
        val currentTime = System.currentTimeMillis()
        if (backPressedTime + backPreventTime > currentTime) {
            onExit()
        } else {
            showToast2(context, "Нажмите ещё раз, чтобы выйти")
            backPressedTime = currentTime
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (
                !state.showDatePicker.value &&
                state.selectedMovie.value == null &&
                state.selectedSeasonalMovie.value == null
            ) {
                TopAppBarMainScreen(
                    title = "Фильмы",
                    onAccountButtonClick = { state.onAccountButtonClick.value = true },
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = {
                        navigateFunction(
                            navController,
                            Route.NotificationsScreen.route
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
                    imageIcon = if (isScrolling) Icons.Default.ArrowUpward else Icons.Default.Settings,
                    contentDescription = stringResource(R.string.description_icon_fab_button_with_menu),
                    expanded = state.menuExpanded.value,
                    onExpandedChange = { state.menuExpanded.value = it },
                    onButtonClick = {
                        if (isScrolling) state.scrollToTop.value = true
                        else state.menuExpanded.value = !state.menuExpanded.value
                    },
                    items = fabMenuItems(
                        isScrolling = isScrolling,
                        onDatePickerToggle = {
                            state.showDatePicker.value = !state.showDatePicker.value
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
                        NewDesignMovieDetailScreen(
                            movie = it,
                            userId = userId,
                            onCloseButton = { state.selectedMovie.value = null },
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
                        is LoadingState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "При загрузке произошла ошибка.",
                                    color = MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        is LoadingState.Success -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {

                                SearchField(state)

                                FetchMovies(
                                    userId = userId,
                                    state = state,
                                    navController = navController,
                                    apiViewModel = apiViewModel,
                                    selectedMovie = { movie ->
                                        state.selectedMovie.value = movie
                                    }
                                )
                            }
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



