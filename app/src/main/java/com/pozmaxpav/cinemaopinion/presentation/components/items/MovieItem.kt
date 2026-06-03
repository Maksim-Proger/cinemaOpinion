package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.utilities.WorkerWithImage

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: MovieData,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        WorkerWithImage(
            movie = movie,
            selectedMovie = null,
            width = 170.dp,
            elevationDp = 0.dp
        )
    }
}



