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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.components.CustomSearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.DetailsCardFilm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Блок поиска
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }
    var searchCompleted by remember { mutableStateOf(false) } // Флаг для отображения списка фильмов после поиска
    val searchHistory = mutableListOf<String>()

    var onFilterButtonClick by remember { mutableStateOf(false) }
    var onAccountButtonClick by remember { mutableStateOf(false) }

    var titleTopBarStae by remember { mutableStateOf(false) }

    // Работаем с ViewModel
    val viewModel: MainViewModel = hiltViewModel()
    val premiereMovies = viewModel.premiersMovies.collectAsState()
    val topListMovies = viewModel.topListMovies.collectAsState()
    val searchMovies = viewModel.searchMovies.collectAsState()

    // Сохраняем выбранный фильм для отправки информации о нем в DetailsCardFilm()
    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }

    // Используем LaunchedEffect для вызова методов выборки при первом отображении Composable.
    LaunchedEffect(Unit) {
        viewModel.fetchPremiersMovies(2023, "July") // TODO: Надо добавить возможность выбора даты
        viewModel.fetchTopListMovies(1) // TODO: Надо разобраться как настроить переключение страницы
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!searchBarActive) {
                CustomTopAppBar(
                    title = if (!titleTopBarStae) {
                        stringResource(id = R.string.top_app_bar_header_name_all_movies)
                    } else {
                        stringResource(id = R.string.top_app_bar_header_name_top_list_movies)
                    },
                    onSearchButtonClick = { searchBarActive = !searchBarActive },
                    onAccountButtonClick = { onAccountButtonClick = true },
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
                FabButton(
                    imageIcon = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.description_floating_action_button_settings),
                    textFloatingButton = stringResource(id = R.string.floating_action_button_settings),
                    onButtonClick = {
                        onFilterButtonClick = !onFilterButtonClick
                        titleTopBarStae = !titleTopBarStae
                        searchCompleted = false
                    },
                )
            }
        }
    ) { padding ->
        AnimatedVisibility( // предупреждение для searchBarActive из-за AnimatedVisibility
            visible = searchBarActive,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
//            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut() // TODO: не работает скрытие
        ) {
            Column(modifier = Modifier.padding(padding)) {
                if (searchBarActive) {
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


