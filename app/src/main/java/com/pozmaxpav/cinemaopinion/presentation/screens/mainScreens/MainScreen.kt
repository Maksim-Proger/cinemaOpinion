package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.CustomSearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.DatePickerFunction
import com.pozmaxpav.cinemaopinion.presentation.components.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.presentation.components.FabButtonWithMenu
import com.pozmaxpav.cinemaopinion.presentation.components.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.MyDropdownMenuItem
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
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
    val listState = rememberLazyListState()
    val isExpanded by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }
    val isScrolling = remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }
    var scrollToTop by remember { mutableStateOf(false) }

    // Логика переключения страницы
    var currentPage by remember { mutableIntStateOf(1) }
    var showNextPageButton by remember { mutableStateOf(false) }
    var saveSearchQuery by remember { mutableStateOf("") }

    // Сохраняем выбранный фильм для отправки информации о нем в DetailsCardFilm()
    var selectedMovie by remember { mutableStateOf<MovieData?>(null) } // TODO: надо разобраться как это работает

    // Используем LaunchedEffect для вызова методов выборки при первом отображении Composable.
    LaunchedEffect(Unit) {
        viewModel.fetchPremiersMovies(2023, "July")
        viewModel.fetchTopListMovies(currentPage)
    }

    // Эффект, который реагирует на изменение scrollToTop и прокручивает список
    LaunchedEffect(scrollToTop) {
        if (scrollToTop) {
            listState.animateScrollToItem(0)
            scrollToTop = false
        }
    }

    // TODO: Добавить проверку, чтобы не пытался переключить на несуществующую страницу и добавить для поиска
    // Эффект, который будет зависеть от состояния списка (для переключения страницы)
    LaunchedEffect(Unit) {
        snapshotFlow { listState.layoutInfo } // Создаем поток, который будет отслеживать изменения в состоянии layoutInfo списка
            .collect { layoutInfo -> // Подписываемся на изменения в этом потоке
                val totalItems = layoutInfo.totalItemsCount // Получаем общее количество элементов в списке

                // Получаем индекс последнего видимого элемента; если нет видимых элементов, устанавливаем 0
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                // Если достигнут конец списка, показываем кнопку "Следующая страница"
                // Устанавливаем showNextPageButton в true, если последний видимый элемент - это последний элемент списка и фильтр активен
                showNextPageButton = lastVisibleItemIndex >= totalItems - 1 && onFilterButtonClick or searchCompleted // TODO: Разобраться с логическими условиями в Kotlin
            }
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
            if (!onAccountButtonClick && !searchBarActive && selectedMovie == null) {
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
                                    painter = painterResource(id = R.drawable.ic_movies),
                                    contentDescription = stringResource(id = R.string.description_icon_settings),
                                    tint = MaterialTheme.colorScheme.onSecondary
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
                                        Icons.Default.DateRange,
                                        contentDescription = stringResource(id = R.string.drop_down_menu_item_select_date),
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )
                        }

                        if (isScrolling.value) {
                            MyDropdownMenuItem(
                                onAction = { scrollToTop = true },
                                title = "Вверх",
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.ArrowUpward,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            )
                        }
                    },
                    expanded = isExpanded
                )
            }

            // Получение выбранной даты TODO: Доработать, нужен только месяц и год!!!
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
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Column(modifier = Modifier.padding(padding)) {
                CustomSearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { searchQuery ->

                        Log.d("@@@", "CustomSearchBar = $currentPage")

                        viewModel.fetchSearchMovies(searchQuery, currentPage)
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
        }

        if (!searchBarActive) {
            if (selectedMovie != null) {
                DetailsCardFilm(
                    stringResource(R.string.movie_has_been_added),
                    stringResource(R.string.movie_has_already_been_added),
                    stringResource(R.string.movie_has_been_added_to_general_list),
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
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(moviesToDisplay) { movie -> // TODO: Подумать, можно ли все прировнять к общему ID?
                        MovieItem(movie = movie) {
                            selectedMovie = movie
                        }
                    }

                    // TODO: При поиске не переключает на последнюю страницу. ПРОВЕРИТЬ!
                    // Добавляем новый элемент
                    if (showNextPageButton) {
                        item {
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                IconButton (
                                    onClick = {
                                        currentPage--
                                        if (onFilterButtonClick) {
                                            viewModel.fetchTopListMovies(currentPage)
                                        } else if (searchCompleted) {
                                            viewModel.fetchSearchMovies(saveSearchQuery, currentPage)
                                        }
                                    },
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Icon(
                                        // TODO: Поправить содержание
                                        painter = painterResource(R.drawable.ic_previous_page),
                                        contentDescription = stringResource(id = R.string.description_icon_home_button),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        currentPage++
                                        if (onFilterButtonClick) {
                                            viewModel.fetchTopListMovies(currentPage)
                                        } else if (searchCompleted) {
                                            viewModel.fetchSearchMovies(saveSearchQuery, currentPage)
                                        }
                                    },
                                    modifier = Modifier.wrapContentWidth()
                                ) {
                                    Icon(
                                        // TODO: Поправить содержание
                                        painter = painterResource(R.drawable.ic_next_page),
                                        contentDescription = stringResource(id = R.string.description_icon_home_button),
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
}


