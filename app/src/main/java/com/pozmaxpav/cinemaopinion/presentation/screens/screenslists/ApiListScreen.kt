package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardFilm
import com.pozmaxpav.cinemaopinion.presentation.components.items.MovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel

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

    val topListMovies by apiViewModel.topListMovies.collectAsStateWithLifecycle()
    val searchMovies by apiViewModel.searchMovies.collectAsStateWithLifecycle()
    val searchMovies2 by apiViewModel.searchMovies2.collectAsStateWithLifecycle()
    val premiereMovies by apiViewModel.premiersMovies.collectAsStateWithLifecycle()

    val items: List<MovieData> = when (listType) {
        "top" -> topListMovies
        "search" -> searchMovies?.items?.takeIf { it.isNotEmpty() } ?: searchMovies2?.films ?: emptyList()
        "premiere" -> premiereMovies
        else -> emptyList()
    }

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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items, key = { it.id }) { movie ->
                MovieItem(
                    modifier = Modifier.animateItem(),
                    movie = movie
                ) {
                    selectedMovie = movie
                }
            }
        }
    }
}
