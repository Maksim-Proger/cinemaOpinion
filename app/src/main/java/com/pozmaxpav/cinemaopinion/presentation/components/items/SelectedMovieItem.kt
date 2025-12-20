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
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage

@Composable
fun SelectedMovieItem(
    movie: DomainSelectedMovieModel,
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
        WorkerWithImage(movie = null, selectedMovie = movie, width = 120.dp)
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = movie.nameFilm,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}