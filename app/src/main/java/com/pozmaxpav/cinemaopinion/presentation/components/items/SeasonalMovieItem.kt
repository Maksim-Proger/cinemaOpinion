package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.utilities.WorkerWithImage

@Composable
fun SeasonalMovieItem(
    modifier: Modifier = Modifier,
    movie: DomainSelectedMovieModel,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        WorkerWithImage(
            movie = null,
            selectedMovie = movie,
            width = 170.dp,
            elevationDp = 0.dp
        )
    }
}
