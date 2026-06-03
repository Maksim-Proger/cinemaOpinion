package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.items.SeasonalMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.seasonal.FetchSeasonalMovies
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SystemMovieViewModel
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction
import java.time.LocalDate

@Composable
fun FetchMovies(
    userId: String,
    state: MainScreenState,
    navController: NavHostController,
    apiViewModel: ApiViewModel,
    selectedMovie: (MovieData) -> Unit,
    systemMovieViewModel: SystemMovieViewModel = hiltViewModel(),
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel()
) {
    val listSelectedMovies by personalMovieViewModel.listSelectedMovies.collectAsState()
    val premiereMovies by apiViewModel.premiersMovies.collectAsStateWithLifecycle()
    val topListMovies by apiViewModel.topListMovies.collectAsStateWithLifecycle()
    val isInitialized = apiViewModel.isInitialized // Флаг для отправки запроса к Api

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            apiViewModel.fetchPremiersMovies(getYear(), getNameMonth())
            apiViewModel.fetchTopListMovies(state.currentPage.intValue)
        }
    }
    LaunchedEffect(userId) {
        personalMovieViewModel.getMovies(userId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = state.listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!state.searchCompleted.value) {
            item {
                FetchSeasonalMovies(
                    viewModel = systemMovieViewModel,
                    selectedMovie = { movie ->
                        state.selectedSeasonalMovie.value = movie
                    }
                )
                Spacer(Modifier.padding(vertical = 5.dp))
            }
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.favourites_movies),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        modifier = Modifier.clickable(
                            onClick = {
                                navigateFunction(navController, Route.ListSelectedMovies.route)
                            }
                        ),
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.favouriteListState
                ) {
                    items(listSelectedMovies, key = { it.id }) { movie ->
                        SeasonalMovieItem(
                            modifier = Modifier.animateItem(),
                            movie = movie
                        ) {
                            state.selectedFavoriteMovie.value = movie
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 5.dp))
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.movies_of_the_month),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.premiereListState
                ) {
                    items(premiereMovies, key = { it.id }) { movie ->
                        MovieItem(
                            modifier = Modifier.animateItem(),
                            movie = movie
                        ) {
                            selectedMovie(movie)
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 5.dp))
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.top_movies),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.topListState
                ) {
                    items(topListMovies, key = { it.id }) { movie ->
                        MovieItem(
                            modifier = Modifier.animateItem(),
                            movie = movie
                        ) {
                            selectedMovie(movie)
                        }
                    }
                }
            }
        }
    }
}






















//@Composable
//fun FetchMovies(
//    state: MainScreenState,
//    apiViewModel: ApiViewModel,
//    selectedMovie: (MovieData) -> Unit
//) {
//
//    val premiereMovies = apiViewModel.premiersMovies.collectAsState()
//    val topListMovies = apiViewModel.topListMovies.collectAsState()
//    val searchMovies = apiViewModel.searchMovies.collectAsState()
//    val searchMovies2 = apiViewModel.searchMovies2.collectAsState()
//
//    val isInitialized = apiViewModel.isInitialized // Флаг для отправки запроса к Api
//
//    LaunchedEffect(Unit) {
//        if (!isInitialized) {
//            apiViewModel.fetchPremiersMovies(getYear(), getNameMonth())
//            apiViewModel.fetchTopListMovies(state.currentPage.intValue)
//        }
//    }
//
//    val moviesToDisplay: List<MovieData> = when {
//        state.searchCompleted.value -> {
//            val firstMovieDatabase = searchMovies.value
//            val secondMovieDatabase = searchMovies2.value
//            if (firstMovieDatabase != null && firstMovieDatabase.items.isNotEmpty()) firstMovieDatabase.items
//            else secondMovieDatabase?.films ?: emptyList()
//        }
//
//        state.isTopMoviesSelected.value -> topListMovies.value?.films ?: emptyList()
//        else -> premiereMovies.value?.items ?: emptyList()
//    }
//
//    // Проверяем возможность активации кнопок для навигации
//    val countPages: Int = when {
//        state.searchCompleted.value -> {
//            val mainMovies = searchMovies.value
//            val fallbackMovies = searchMovies2.value
//            if (mainMovies != null && mainMovies.totalPages != 0) mainMovies.totalPages
//            else fallbackMovies?.pagesCount ?: 0
//        }
//
//        state.isTopMoviesSelected.value -> topListMovies.value?.pagesCount ?: 0
//        else -> 0
//    }
//    val canGoBack = state.currentPage.intValue > 1
//    val canGoForward = state.currentPage.intValue < countPages
//
//    Column {
//        LazyColumn(
//            state = state.listState,
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentPadding = PaddingValues(13.dp)
//        ) {
//            items(moviesToDisplay, key = { it.id }) { movie ->
//                MovieItem(
//                    modifier = Modifier.animateItem(),
//                    movie = movie
//                ) {
//                    selectedMovie(movie)
//                }
//            }
//        }
//
//        PageSwitch(state, canGoBack, apiViewModel, canGoForward)
//    }
//
//}

private fun getYear(): Int {
    val currentDate = LocalDate.now()
    return currentDate.year
}

private fun getNameMonth(): String {
    val currentDate = LocalDate.now()
    val currentMonthName = currentDate.month.name
    return currentMonthName.lowercase().replaceFirstChar { it.uppercase() }
}