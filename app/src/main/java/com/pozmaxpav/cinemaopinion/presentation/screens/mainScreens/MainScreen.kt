package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest
import com.pozmaxpav.cinemaopinion.presentation.components.CustomBoxShowOverlay
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.CustomSearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.DatePickerFunction
import com.pozmaxpav.cinemaopinion.presentation.components.FabButtonWithMenu
import com.pozmaxpav.cinemaopinion.presentation.components.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.MyCustomDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.components.NewYearMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.PageDescription
import com.pozmaxpav.cinemaopinion.presentation.components.PreviewAlertDialog
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCard
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.SearchFilterScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.NODE_NEW_YEAR_LIST
import com.pozmaxpav.cinemaopinion.utilits.formatMonth
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.state.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    fireBaseMovieViewModel: FireBaseMovieViewModel = hiltViewModel()
) {

    // region Переменные

    // region DatePicker
    var selectedDate by remember { mutableStateOf<Pair<Int, String>?>(null) } // Значение выбранной даты
    var dateSelectionComplete by remember { mutableStateOf(false) } // Флаг подтверждения, что дата выбрана и можно отправлять запрос
    // endregion

    // region Блок поиска
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }
    var searchCompleted by remember { mutableStateOf(false) } // Флаг для отображения списка фильмов после поиска
    val searchHistory = mutableListOf<String>() // TODO: Не работает
    // endregion

    // region Расширенный поиск
    var requestBody by remember {
        mutableStateOf(
            CompositeRequest(
                null, null, null, null, null,
                null, null
            )
        )
    }
    var sendRequestCompleted by remember { mutableStateOf(false) } // Флаг для предотвращения повторной отправки запроса
    // endregion

    // region Состояния для открытия страниц
    var showDatePicker by remember { mutableStateOf(false) }
    var onFilterButtonClick by remember { mutableStateOf(false) }
    var onAccountButtonClick by remember { mutableStateOf(false) }
    var onAdvancedSearchButtonClick by remember { mutableStateOf(false) }
    var locationShowDialogEvents by remember { mutableStateOf(false) }
    var locationShowPageAppDescription by remember { mutableStateOf(false) }
    // endregion

    // region Работаем с ViewModel
    val premiereMovies = apiViewModel.premiersMovies.collectAsState()
    val topListMovies = apiViewModel.topListMovies.collectAsState()
    val searchMovies = apiViewModel.searchMovies.collectAsState()
    val searchMovies2 = apiViewModel.searchMovies2.collectAsState()
    val newYearMoviesList by fireBaseMovieViewModel.movies.collectAsState()
    val state by apiViewModel.state.collectAsState()
    val showDialogEvents by mainViewModel.resultChecking.collectAsState()
    // endregion

    // region Работаем с Fab
    val listState = rememberLazyListState()
    val lisStateRow = rememberLazyListState()
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val isScrolling = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    var scrollToTop by remember { mutableStateOf(false) }
    // endregion

    var titleTopBarState by remember { mutableStateOf(false) } // Заголовок для AppBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isInitialized = apiViewModel.isInitialized // Флаг для отправки запроса к Api
    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }
    var selectedNewYearMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }

    // region Логика переключения страницы
    var currentPage by remember { mutableIntStateOf(1) }
    var showPageSwitchingButtons by remember { mutableStateOf(false) }
    var saveSearchQuery by remember { mutableStateOf("") } // Сохраняем содержание поиска
    // endregion

    // endregion

    // region Launchers
    LaunchedEffect(Unit) {
        if (!isInitialized) {
            apiViewModel.fetchPremiersMovies(2025, "March")
            apiViewModel.fetchTopListMovies(currentPage)
        }
    }
    LaunchedEffect(onAccountButtonClick) {
        fireBaseMovieViewModel.getMovies(NODE_NEW_YEAR_LIST)
    }
    LaunchedEffect(scrollToTop) {
        if (scrollToTop) {
            listState.animateScrollToItem(0)
            scrollToTop = false
        }
    } // Эффект, который реагирует на изменение scrollToTop и прокручивает список
    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo } // Создаем поток, который будет отслеживать изменения в состоянии layoutInfo списка
            .collect { layoutInfo -> // Подписываемся на изменения в этом потоке
                val totalItems =
                    layoutInfo.totalItemsCount // Получаем общее количество элементов в списке

                // Получаем индекс последнего видимого элемента; если нет видимых элементов, устанавливаем 0
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                // Если достигнут конец списка, показываем кнопку "Следующая страница"
                // Устанавливаем showPageSwitchingButtons в true, если последний видимый элемент - это последний элемент списка и фильтр активен
                showPageSwitchingButtons = lastVisibleItemIndex >= totalItems - 1
            }
    } // Эффект, который будет зависеть от состояния списка (для переключения страницы)
    // endregion

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!searchBarActive) {
                CustomTopAppBar(
                    title = if (!titleTopBarState) {
                        stringResource(id = R.string.top_app_bar_header_name_all_movies)
                    } else {
                        stringResource(id = R.string.top_app_bar_header_name_top_list_movies)
                    },
                    onSearchButtonClick = { searchBarActive = !searchBarActive },
                    onAdvancedSearchButtonClick = {
                        onAdvancedSearchButtonClick = !onAdvancedSearchButtonClick
                    },
                    onAccountButtonClick = { onAccountButtonClick = !onAccountButtonClick },
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
                !searchBarActive && !onAdvancedSearchButtonClick && selectedMovie == null &&
                selectedNewYearMovie == null && !showDatePicker && !locationShowPageAppDescription
            ) {
                FabButtonWithMenu(
                    imageIcon = if (isScrolling.value) Icons.Default.ArrowUpward else Icons.Default.Settings,
                    contentDescription = "Меню настроек",
                    textFloatingButton = if (isScrolling.value) "" else "Настройки",
                    content = {
                        if (!isScrolling.value) {
                            MyCustomDropdownMenuItem(
                                onAction = {
                                    onFilterButtonClick = !onFilterButtonClick
                                    titleTopBarState = !titleTopBarState
                                    searchCompleted = false
                                },
                                title = if (!titleTopBarState) {
                                    stringResource(id = R.string.drop_down_menu_item_premiere_movies)
                                } else {
                                    stringResource(id = R.string.drop_down_menu_item_topList_movies)
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_movies),
                                        contentDescription = stringResource(id = R.string.description_icon_settings),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )

                            if (!titleTopBarState) {
                                MyCustomDropdownMenuItem(
                                    onAction = { showDatePicker = !showDatePicker },
                                    title = stringResource(id = R.string.drop_down_menu_item_select_date),
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.DateRange,
                                            contentDescription = stringResource(id = R.string.drop_down_menu_item_select_date),
                                            tint = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                )
                            }

                            MyCustomDropdownMenuItem(
                                onAction = {
                                    navigateFunction(navController, Route.MediaNewsScreen.route)
                                },
                                title = "Интересное",
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Newspaper,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )
                        }
                    },
                    onButtonClick = { // TODO: Надо еще раз подумать на этой кнопкой, не хочу чтобы при поднятии вверх открывалось меню.
                        if (isScrolling.value) {
                            scrollToTop = true
                        }
                    },
                    expanded = isExpanded
                )
            }
        }
    ) { padding ->

        Box(modifier = Modifier.fillMaxSize()) {

            if (dateSelectionComplete) {
                selectedDate?.let {
                    apiViewModel.fetchPremiersMovies(it.first, formatMonth(it.second))
                    dateSelectionComplete = false
                }
            }

            if (sendRequestCompleted) {
                requestBody.let { compositeRequest ->
                    apiViewModel.searchFilmsByFilters(
                        compositeRequest.type,
                        compositeRequest.keyword,
                        compositeRequest.countries,
                        compositeRequest.genres,
                        compositeRequest.ratingFrom,
                        compositeRequest.yearFrom,
                        compositeRequest.yearTo,
                        currentPage
                    )
                    sendRequestCompleted = false
                }
            }

            if (!searchBarActive) {
                if (selectedMovie != null) {
                    DetailsCardFilm(
                        movie = selectedMovie!!,
                        onClick = { selectedMovie = null },
                        padding = padding,
                    )
                    BackHandler { selectedMovie = null }

                } else if (selectedNewYearMovie != null) {
                    DetailsCard(
                        selectedNewYearMovie!!,
                        onCloseButton = { selectedNewYearMovie = null },
                        padding
                    )
                    BackHandler { selectedNewYearMovie = null }
                } else {
                    val moviesToDisplay: List<MovieData> = when {
                        searchCompleted -> {
                            val mainMovies = searchMovies.value
                            val fallbackMovies = searchMovies2.value
                            if (mainMovies != null && mainMovies.items.isNotEmpty()) mainMovies.items
                            else fallbackMovies?.films ?: emptyList()
                        }

                        onFilterButtonClick -> topListMovies.value?.films ?: emptyList()
                        else -> premiereMovies.value?.items ?: emptyList()
                    }
                    val countPages: Int = when {
                        searchCompleted -> {
                            val mainMovies = searchMovies.value
                            val fallbackMovies = searchMovies2.value
                            if (mainMovies != null && mainMovies.totalPages != 0) mainMovies.totalPages
                            else fallbackMovies?.pagesCount ?: 0
                        }

                        onFilterButtonClick -> topListMovies.value?.pagesCount ?: 0
                        else -> 0
                    }

                    // Проверяем возможность активации кнопок для навигации
                    val canGoBack = currentPage > 1
                    val canGoForward = currentPage < countPages

                    when (state) {
                        is State.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLottieAnimation(
                                    nameFile = "loading_animation.lottie",
                                    modifier = Modifier.scale(0.5f)
                                )
                            }
                        }

                        is State.Success -> {
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)) {
//                                SeasonalEventList(
//                                    isScrolling,
//                                    lisStateRow,
//                                    newYearMoviesList,
//                                    selectedNewYearMovie
//                                )

                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentPadding = PaddingValues(13.dp)
                                ) {
                                    items(moviesToDisplay, key = { it.id }) { movie ->
                                        MovieItem(movie = movie) { selectedMovie = movie }
                                    }

                                    if (showPageSwitchingButtons) {
                                        item {
                                            Row(
                                                modifier = Modifier
                                                    .wrapContentWidth()
                                                    .padding(vertical = 16.dp)
                                            ) {
                                                if (canGoBack) {
                                                    IconButton(
                                                        onClick = {
                                                            currentPage--
                                                            if (onFilterButtonClick) {
                                                                apiViewModel.fetchTopListMovies(
                                                                    currentPage
                                                                )
                                                            } else if (searchCompleted) {
                                                                apiViewModel.fetchSearchMovies(
                                                                    saveSearchQuery,
                                                                    currentPage
                                                                )
                                                            }
                                                            scrollToTop = true
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
                                                            currentPage++
                                                            if (onFilterButtonClick) {
                                                                apiViewModel.fetchTopListMovies(
                                                                    currentPage
                                                                )
                                                            } else if (searchCompleted) {
                                                                apiViewModel.fetchSearchMovies(
                                                                    saveSearchQuery,
                                                                    currentPage
                                                                )
                                                            }
                                                            scrollToTop = true
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
                                }
                            }
                        }

                        is State.Error -> {}
                    }
                }
            }

            if (showDatePicker) {
                DatePickerFunction(
                    onCloseDatePicker = {
                        showDatePicker = !showDatePicker
                        dateSelectionComplete = true
                    },
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                )
                BackHandler {
                    showDatePicker = !showDatePicker
                }
            }

        }
    }

    if (searchBarActive) {
        CustomBoxShowOverlay(
            paddingVerticalSecondBox = 50.dp,
            paddingHorizontalSecondBox = 16.dp,
            content = {
                CustomSearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { searchQuery ->
                        currentPage = 1
                        apiViewModel.fetchSearchMovies(searchQuery, currentPage)
                        saveSearchQuery = searchQuery
                        searchHistory.add(searchQuery)
                        searchCompleted = true
                        searchBarActive = false
                    },
                    active = searchBarActive,
                    onActiveChange = { isActive -> searchBarActive = isActive },
                    searchHistory = searchHistory
                )
            }
        )
    }

    if (!showDialogEvents && !locationShowDialogEvents) {
        CustomBoxShowOverlay(
            content = {
                PreviewAlertDialog {
                    locationShowDialogEvents = true
                    locationShowPageAppDescription = true
                }
            }
        )
    }

    if (locationShowPageAppDescription) {
        var flag by remember { mutableStateOf(false) }
        CustomBoxShowOverlay(
            content = {
                PageDescription(
                    onDismiss = {
                        locationShowPageAppDescription = false
                        flag = true
                    }
                )
            }
        )
        LaunchedEffect(flag) {
            mainViewModel.resetResultChecking()
        }
    }

    if (onAdvancedSearchButtonClick) {
        CustomBoxShowOverlay(
            content = {
                SearchFilterScreen(
                    onClickClose = { onAdvancedSearchButtonClick = false },
                    onSendRequest = { sendRequestCompleted = true },
                    onSearch = {
                        requestBody = it
                        searchCompleted = true
                    }
                )
                BackHandler {
                    onAdvancedSearchButtonClick = false
                }
            }
        )
    }

    if (onAccountButtonClick) {
        CustomBoxShowOverlay(
            onDismiss = {
                onAccountButtonClick = false
            },
            paddingVerticalSecondBox = 50.dp,
            paddingHorizontalSecondBox = 16.dp,
            content = {
                AccountScreen(
                    navController,
                    onClick = { onAccountButtonClick = false }
                )
                BackHandler {
                    onAccountButtonClick = false
                }
            }
        )
    }

}

@Composable
private fun SeasonalEventList( // Пока только для нового года
    isScrolling: androidx.compose.runtime.State<Boolean>,
    lisStateRow: LazyListState,
    newYearMoviesList: List<DomainSelectedMovieModel>,
    selectedNewYearMovie: DomainSelectedMovieModel?
) {
    var selectedNewYearMovie1 = selectedNewYearMovie
    AnimatedVisibility(
        visible = !isScrolling.value,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(durationMillis = 300)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(durationMillis = 300)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.title_of_the_list_for_the_new_year),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(modifier = Modifier.padding(6.dp))
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    ),
                state = lisStateRow
            ) {
                items(newYearMoviesList, key = { it.id }) { newYearMovie ->
                    NewYearMovieItem(newYearMovie = newYearMovie) {
                        selectedNewYearMovie1 = newYearMovie
                    }
                }
            }
        }
    }
}


