package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.R
//import com.pozmaxpav.cinemaopinion.domain.models.Movie
import com.pozmaxpav.cinemaopinion.domain.models.MovieData
//import com.pozmaxpav.cinemaopinion.domain.models.MovieTopList
import com.pozmaxpav.cinemaopinion.presentation.components.SearchBar
import com.pozmaxpav.cinemaopinion.presentation.components.TopAppBar
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.ui.theme.CinemaOpinionTheme
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var query by remember { mutableStateOf("") }
    var searchBarActive by remember { mutableStateOf(false) }
    var filterBarActive by remember { mutableStateOf(false) }
    var onAccountButtonClick by remember { mutableStateOf(false) }

    // Работаем с VoewModel
    val viewModel: MainViewModel = hiltViewModel()
    val premiereMovies = viewModel.premiersMovies.collectAsState()
    val topListMovies = viewModel.topListMovies.collectAsState()

    // Сохраняем выбранный фильм для отправки информации о нем в DetailsCardFilm()
    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }

    // Используем LaunchedEffect для вызова методов выборки при первом отображении Composable.
    LaunchedEffect(Unit) {
        viewModel.fetchPremiersMovies(2022, "July")
        viewModel.fetchTopListMovies(1) // TODO: Надо разобраться как настроить переключение страницы
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.top_app_bar_header_name),
                onSearchButtonClick = { searchBarActive = !searchBarActive },
                onFilterButtonClick = { filterBarActive = true },
                onAccountButtonClick = { onAccountButtonClick = true },
                scrollBehavior = scrollBehavior
            )

            if (onAccountButtonClick) {
                AccountScreen(
                    navController,
                    onClick = { onAccountButtonClick = false } // Закрытие диалога при нажатии
                )
            }
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {
            if (searchBarActive) {
                SearchBar(
                    query = query,
                    onQueryChange = { newQuery -> query = newQuery },
                    onSearch = { searchQuery -> /* обработка поиска */ },
                    active = searchBarActive,
                    onActiveChange = { isActive -> searchBarActive = isActive }
                )
            }
        }
        /**
         * query = query: Передает текущее значение запроса.
         * onQueryChange = { newQuery -> query = newQuery }: Обновляет состояние query при изменении текста в поле поиска.
         * onSearch = { searchQuery -> /* обработка поиска */ }: Позволяет обработать поиск, когда пользователь выполняет поиск.
         * active = searchBarActive: Управляет состоянием активности SearchBar.
         * onActiveChange = { isActive -> searchBarActive = isActive }: Обновляет состояние активности поиска.
         */

        if (!searchBarActive) {
            if (selectedMovie != null) {
                DetailsCardFilm(selectedMovie!!, onClick = { selectedMovie = null }, padding)
            } else {

                // Определяем, какие данные будут отображаться в зависимости от состояния filterBarActive
                val moviesToDisplay: List<MovieData> = if (!filterBarActive) {
                    premiereMovies.value?.items ?: emptyList<MovieData>()
                } else {
                    topListMovies.value?.films ?: emptyList<MovieData>()
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


@Composable
fun MovieItem(
    movie: MovieData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            WorkerWithImage(movie, 150.dp)

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                when(movie) {
                    is MovieData.Movie -> {
                        Column {
                            Text(
                                text = movie.nameRu,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = movie.premiereRu,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    is MovieData.MovieTopList -> {}
                }
            }
        }
    }
}
