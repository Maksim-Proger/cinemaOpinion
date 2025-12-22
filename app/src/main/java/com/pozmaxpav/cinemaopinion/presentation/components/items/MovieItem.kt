package com.pozmaxpav.cinemaopinion.presentation.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage
import com.pozmaxpav.cinemaopinion.utilits.formatCountries
import com.pozmaxpav.cinemaopinion.utilits.formatDate2
import com.pozmaxpav.cinemaopinion.utilits.formatGenres

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: MovieData,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
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
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            WorkerWithImage(movie = movie, selectedMovie = null, width = 120.dp)
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))

            Column {
                Spacer(Modifier.padding(vertical = 10.dp))
                Text(
                    text = movie.nameRu ?: "Нет названия",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                when (movie) {
                    is MovieData.Movie -> {
                        MetaText(
                            year = formatDate2(date = movie.premiereRu),
                            genre = formatGenres(movie.genres),
                            country = formatCountries(country = movie.countries)
                        )
                    }
                    is MovieData.MovieTop -> {
                        MetaText(
                            year = movie.year,
                            genre = formatGenres(movie.genres),
                            country = formatCountries(country = movie.countries)
                        )
                    }
                    is MovieData.MovieSearch -> {
                        MetaText(
                            year = movie.year ?: "Нет данных о дате",
                            genre = formatGenres(movie.genres),
                            country = formatCountries(country = movie.countries)
                        )
                    }
                    is MovieData.MovieSearch2 -> {
                        MetaText(
                            year = movie.year ?: "Нет данных о дате",
                            genre = formatGenres(movie.genres),
                            country = formatCountries(country = movie.countries)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MetaText(
    year: String = "",
    genre: String = "",
    country: String = ""
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.Schedule,
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(end = 6.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = year,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocalOffer,
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(end = 6.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = genre,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(24.dp).padding(end = 6.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = country,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
