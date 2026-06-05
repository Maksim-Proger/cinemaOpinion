package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.utilities.WorkerWithImage
import com.pozmaxpav.cinemaopinion.R

@Composable
fun SelectedMovieItem(
    movieData: MovieData?,
    selectedMovie: DomainSelectedMovieModel?,
    onClick: () -> Unit,
    showTopBar: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
                showTopBar()
            }
    ) {
        selectedMovie?.let { movie ->
            WorkerWithImage(movie = null, selectedMovie = movie, width = 120.dp)
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = movie.nameFilm,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        movieData?.let { movie ->
            WorkerWithImage(movie = movie, selectedMovie = null, width = 120.dp)
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = movie.nameRu ?: stringResource(R.string.no_movie_title),
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }
}


