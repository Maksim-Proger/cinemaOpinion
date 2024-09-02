package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.CustomSearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.DatePickerFunction
import com.pozmaxpav.cinemaopinion.presentation.components.FabButtonWithMenu
import com.pozmaxpav.cinemaopinion.presentation.components.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.utilits.formatMonth
import com.pozmaxpav.cinemaopinion.utilits.formatYear
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // DatePicker
    var showDatePicker by remember { mutableStateOf(false) }  // Состояние для показа DatePicker
    // TODO: разобраться что в себе несет эта переменная
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) } // Состояние для выбранной даты

    // Блок поиска
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }
    var searchCompleted by remember { mutableStateOf(false) } // Флаг для отображения списка фильмов после поиска
    val searchHistory = mutableListOf<String>()

    var onFilterButtonClick by remember { mutableStateOf(false) }
    var onAccountButtonClick by remember { mutableStateOf(false) }

    var titleTopBarState by remember { mutableStateOf(false) }

    // Работаем с ViewModel
    val viewModel: MainViewModel = hiltViewModel()
    val premiereMovies = viewModel.premiersMovies.collectAsState()
    val topListMovies = viewModel.topListMovies.collectAsState()
    val searchMovies = viewModel.searchMovies.collectAsState()

    // Работаем с Fab
    val listState = rememberLazyListState() // TODO: что это за переменная
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

    // Сохраняем выбранный фильм для отправки информации о нем в DetailsCardFilm()
    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }

    // Используем LaunchedEffect для вызова методов выборки при первом отображении Composable.
    LaunchedEffect(Unit) {
        viewModel.fetchPremiersMovies(2023, "July") // TODO: Надо разобраться как настроить переключение страницы
        viewModel.fetchTopListMovies(1) // TODO: Надо разобраться как настроить переключение страницы
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(
                visible = !searchBarActive,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                CustomTopAppBar(
                    title = if (!titleTopBarState) {
                        stringResource(id = R.string.top_app_bar_header_name_all_movies)
                    } else {
                        stringResource(id = R.string.top_app_bar_header_name_top_list_movies)
                    },
                    onSearchButtonClick = { searchBarActive = !searchBarActive },
                    onAccountButtonClick = { onAccountButtonClick = !onAccountButtonClick },
                    scrollBehavior = scrollBehavior
                )
            }

            if (onAccountButtonClick) {
                AccountScreen(
                    navController,
                    onClick = { onAccountButtonClick = false }
                )

                // Обработка нажатия системной кнопки "Назад"
                BackHandler {
                    onAccountButtonClick = false
                }
            }
        },

        floatingActionButton = {
            if (!onAccountButtonClick && !searchBarActive) {
                FabButtonWithMenu(
                    imageIcon = Icons.Default.Settings,
                    contentDescription = "Меню настроек",
                    textFloatingButton = "Настройки",
                    content = {
                        MyDropdownMenuItem(
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
                                    Icons.Default.Settings,
                                    contentDescription = stringResource(id = R.string.description_icon_settings)
                                )
                            }
                        )

                        if (!titleTopBarState) {
                            MyDropdownMenuItem(
                                onAction = {
                                    showDatePicker = !showDatePicker
                                },
                                title = stringResource(id = R.string.drop_down_menu_item_select_date),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Settings,
                                        contentDescription = stringResource(id = R.string.description_icon_settings)
                                    )
                                }
                            )
                        }
                    },
                    expanded = isExpanded
                )
            }

            // Получение выбранной даты TODO: Доработать!!!
            selectedDate?.let {
                viewModel.fetchPremiersMovies(formatYear(it.toString()), formatMonth(it.toString()))
            }

            DatePickerFunction(
                showDatePicker,
                onDateSelected = { date ->
                    selectedDate = date
                    showDatePicker = !showDatePicker
                }
            )
        }
    ) { padding ->

        AnimatedVisibility(
            visible = searchBarActive,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.padding(padding)) {
                CustomSearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { searchQuery ->
                        viewModel.fetchSearchMovies(searchQuery)
                        searchHistory.add(searchQuery)
                        searchCompleted = true
                        searchBarActive = false
                    },
                    active = searchBarActive, // TODO: исправить это предупреждение
                    onActiveChange = { isActive -> searchBarActive = isActive },
                    searchHistory = searchHistory
                )
            }
        }

        if (!searchBarActive) {
            if (selectedMovie != null) {
                DetailsCardFilm(
                    selectedMovie!!,
                    onClick = { selectedMovie = null },
                    padding
                )
                BackHandler {
                    selectedMovie = null
                }
            } else {

                val moviesToDisplay: List<MovieData> = when {
                    searchCompleted -> searchMovies.value?.items ?: emptyList()
                    onFilterButtonClick -> topListMovies.value?.films ?: emptyList()
                    else -> premiereMovies.value?.items ?: emptyList()
                }

                LazyColumn(
                    state = listState, // это свойство нужно для анимации FabButton

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(moviesToDisplay) { movie ->
                        MovieItem(movie = movie) {
                            selectedMovie = movie
                        }
                    }
                }
            }
        }
    }
}


