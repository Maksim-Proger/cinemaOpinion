package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.items.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel

@Composable
fun FetchMovies(
    state: MainScreenState,
    apiViewModel: ApiViewModel,
    selectedMovie: (MovieData) -> Unit
) {

    val premiereMovies = apiViewModel.premiersMovies.collectAsState()
    val topListMovies = apiViewModel.topListMovies.collectAsState()
    val searchMovies = apiViewModel.searchMovies.collectAsState()
    val searchMovies2 = apiViewModel.searchMovies2.collectAsState()

    val isInitialized = apiViewModel.isInitialized // Флаг для отправки запроса к Api

    LaunchedEffect(Unit) {
        if (!isInitialized) {
            apiViewModel.fetchPremiersMovies(2025, "December")
            apiViewModel.fetchTopListMovies(state.currentPage.intValue)
        }
    }

    val moviesToDisplay: List<MovieData> = when {
        state.searchCompleted.value -> {
            val firstMovieDatabase = searchMovies.value
            val secondMovieDatabase = searchMovies2.value
            if (firstMovieDatabase != null && firstMovieDatabase.items.isNotEmpty()) firstMovieDatabase.items
            else secondMovieDatabase?.films ?: emptyList()
        }

        state.isTopMoviesSelected.value -> topListMovies.value?.films ?: emptyList()
        else -> premiereMovies.value?.items ?: emptyList()
    }

    // Проверяем возможность активации кнопок для навигации
    val countPages: Int = when {
        state.searchCompleted.value -> {
            val mainMovies = searchMovies.value
            val fallbackMovies = searchMovies2.value
            if (mainMovies != null && mainMovies.totalPages != 0) mainMovies.totalPages
            else fallbackMovies?.pagesCount ?: 0
        }

        state.isTopMoviesSelected.value -> topListMovies.value?.pagesCount ?: 0
        else -> 0
    }
    val canGoBack = state.currentPage.intValue > 1
    val canGoForward = state.currentPage.intValue < countPages

    Column {
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(13.dp)
        ) {
            items(moviesToDisplay, key = { it.id }) { movie ->
                MovieItem(
                    modifier = Modifier.animateItem(),
                    movie = movie
                ) {
                    selectedMovie(movie)
                }
            }
        }

        PageSwitch(state, canGoBack, apiViewModel, canGoForward)
    }

}

