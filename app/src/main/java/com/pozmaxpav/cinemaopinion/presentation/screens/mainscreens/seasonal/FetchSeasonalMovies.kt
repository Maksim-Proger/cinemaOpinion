package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.seasonal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.utils.events.Season
import com.example.core.utils.events.SeasonTitles
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SeasonalMovieItem
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SystemMovieViewModel
import java.time.LocalDate

@Composable
fun FetchSeasonalMovies(
    isScrolling: Boolean,
    viewModel: SystemMovieViewModel = hiltViewModel(),
    selectedMovie: (DomainSelectedMovieModel) -> Unit
) {
    val lisStateRow = rememberLazyListState()
    val seasonalMovies = getSeasonalListMovies(viewModel)

    if (seasonalMovies != null) {
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
            Card(modifier = Modifier.padding(horizontal = 16.dp)) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = getSeasonalTitle(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 24.sp,
                                letterSpacing = 0.7.sp
                            ),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    seasonalMovies.let { list ->
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
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
    }
}

@Composable
private fun getSeasonalTitle(): String {
    val currentMonth = remember { LocalDate.now().monthValue }
    val titleString = remember(currentMonth) {
        when (currentMonth) {
            12, 1 -> SeasonTitles.NewYear.title
            10, 11 -> SeasonTitles.Halloween.title
            else -> ""
        }
    }
    return titleString
}

@Composable
private fun getSeasonalListMovies(viewModel: SystemMovieViewModel): List<DomainSelectedMovieModel>? {
    val currentMonth = remember { LocalDate.now().monthValue }
    val movies by viewModel.list.collectAsState()
    val currentSeason = remember(currentMonth) {
        when (currentMonth) {
            12, 1 -> Season.NewYear
            10, 11 -> Season.Halloween
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
