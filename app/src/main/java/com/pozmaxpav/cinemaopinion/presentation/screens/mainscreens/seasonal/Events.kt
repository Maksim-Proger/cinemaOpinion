package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.seasonal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SeasonalMovieItem
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.Season
import java.time.LocalDate

@Composable
fun FetchSeasonalMovies(
    isScrolling: Boolean,
    viewModel: MovieViewModel,
    selectedMovie: (DomainSelectedMovieModel) -> Unit
) {
    val lisStateRow = rememberLazyListState()
    val seasonalMovies = getSeasonalListMovies(viewModel)

    AnimatedVisibility(
        visible = !isScrolling,
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

            seasonalMovies?.let { list ->
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 16.dp)
                        .background(color = MaterialTheme.colorScheme.surface),
                    state = lisStateRow
                ) {
                    items(list, key = { it.id }) { movie ->
                        SeasonalMovieItem(
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

@Composable
private fun getSeasonalListMovies(
    viewModel: MovieViewModel
): List<DomainSelectedMovieModel>? {

    val currentMonth = remember { LocalDate.now().monthValue }

    val movies by viewModel.movies.collectAsState()

    val currentSeason = remember(currentMonth) {
        when (currentMonth) {
            12, 1, 10 -> Season.NewYear
//            10 -> Season.Halloween
//            2 -> Season.Valentine
//            3 -> Season.March8
            else -> null
        }
    }

    LaunchedEffect(currentSeason) {
        currentSeason?.let {
            viewModel.getMovies(it.node)
        }
    }

    return currentSeason?.let { movies }
}