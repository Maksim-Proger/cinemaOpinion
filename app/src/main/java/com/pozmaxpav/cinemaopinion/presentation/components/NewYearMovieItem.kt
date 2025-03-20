package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel

@Composable
fun NewYearMovieItem(
    newYearMovie: SelectedMovieModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 7.dp)
            .clickable { onClick() }
    ) {
        Row {
            AsyncImage(
                model = newYearMovie.posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}