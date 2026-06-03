package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.seasonal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.core.utils.events.Season
import com.example.core.utils.events.SeasonTitles
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SeasonalMovieItem
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SystemMovieViewModel
import java.time.LocalDate

@Composable
fun FetchSeasonalMovies(
    viewModel: SystemMovieViewModel = hiltViewModel(),
    selectedMovie: (DomainSelectedMovieModel) -> Unit
) {
    val lisStateRow = rememberLazyListState()
    val seasonalMovies = getSeasonalListMovies(viewModel)

    if (seasonalMovies != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = getSeasonalTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = lisStateRow
            ) {
                items(seasonalMovies, key = { it.id }) { movie ->
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

@Composable
private fun getSeasonalTitle(): String {
    val currentMonth = remember { LocalDate.now().monthValue }
    val titleString = remember(currentMonth) {
        when (currentMonth) {
            12, 1, 6 -> SeasonTitles.NewYear.title
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
            12, 1, 6 -> Season.NewYear
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
