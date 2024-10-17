package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData
import com.pozmaxpav.cinemaopinion.utilits.formatCountries
import com.pozmaxpav.cinemaopinion.utilits.formatGenres
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage

@Composable
fun MovieItem(
    movie: MovieData,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
        ) {

            WorkerWithImage(movie, 150.dp)
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = movie.nameRu ?: "Нет названия",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = formatCountries(movie.countries),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(7.dp))

                when (movie) {
                    is MovieData.Movie -> {
                        Text(
                            text = movie.premiereRu,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = formatGenres(movie.genres),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is MovieData.MovieTop -> {
                        Text(
                            text = movie.year,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = movie.rating,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is MovieData.MovieSearch -> {
                        Text(
                            text = movie.year,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = "Рейтинг Kinopoisk: ${movie.ratingKinopoisk ?: "Нет данных о рейтинге"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        Text(
                            text = "Рейтинг Imdb: ${movie.ratingImdb ?: "Нет данных о рейтинге"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}