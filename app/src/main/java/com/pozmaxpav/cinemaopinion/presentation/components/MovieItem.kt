package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.MovieData
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage

@Composable
fun MovieItem(movie: MovieData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {

            WorkerWithImage(movie, 150.dp)

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = movie.nameRu ?: "Нет названия",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.year ?: "Нет года выпуска",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.countries.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))

                when (movie) {
                    is MovieData.Movie -> {
                        Text(
                            text = movie.genres.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is MovieData.MovieTop -> {
                        Text(
                            text = movie.rating.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is MovieData.MovieSearch -> {}
                }
            }
        }
    }
}