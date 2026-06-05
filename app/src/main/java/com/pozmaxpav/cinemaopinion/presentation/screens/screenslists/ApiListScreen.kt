package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.presentation.components.items.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ApiListScreen(
    navController: NavHostController,
    listType: String,
    userId: String
) {
    val parentEntry = remember(navController) {
        navController.previousBackStackEntry!!
    }
    val apiViewModel = hiltViewModel<ApiViewModel>(parentEntry)

    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }

    if (selectedMovie != null) {
        DetailsCardFilm(
            movie = selectedMovie,
            userId = userId,
            onCloseButton = { selectedMovie = null },
            padding = PaddingValues(),
            navController = navController
        )
        AdaptiveBackHandler { selectedMovie = null }
    } else {
        when (listType) {
            "premiere" -> {
                val premiereMovies by apiViewModel.premiersMovies.collectAsStateWithLifecycle()
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(premiereMovies, key = { it.id }) { movie ->
                        MovieItem(
                            modifier = Modifier.animateItem(),
                            movie = movie
                        ) { selectedMovie = movie }
                    }
                }
            }
            else -> {
                val pagingFlow = remember(listType) {
                    when (listType) {
                        "top" -> apiViewModel.topMoviesPaging
                        "search" -> apiViewModel.searchMoviesPaging
                        else -> emptyFlow<PagingData<MovieData>>()
                    }
                }
                PagedMovieList(
                    movies = pagingFlow.collectAsLazyPagingItems(),
                    onMovieClick = { selectedMovie = it }
                )
            }
        }
    }
}

@Composable
private fun PagedMovieList(
    movies: LazyPagingItems<MovieData>,
    onMovieClick: (MovieData) -> Unit
) {
    when (movies.loadState.refresh) {
        is LoadState.Loading -> CenteredBox {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        }
        is LoadState.Error -> CenteredBox {
            Text(
                text = "При загрузке произошла ошибка.",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = movies.itemCount,
                    key = movies.itemKey { it.id }
                ) { index ->
                    movies[index]?.let { movie ->
                        MovieItem(
                            modifier = Modifier.animateItem(),
                            movie = movie
                        ) { onMovieClick(movie) }
                    }
                }
                if (movies.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteredBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
