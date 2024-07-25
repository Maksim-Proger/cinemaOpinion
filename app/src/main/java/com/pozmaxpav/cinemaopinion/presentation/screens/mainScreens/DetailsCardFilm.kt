package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.Movie
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage

@Composable
fun DetailsCardFilm(
    movie: Movie,
    onClick: () -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    WorkerWithImage(movie, 250.dp)

                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = movie.nameRu,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = movie.premiereRu,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = movie.genres.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = movie.countries.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

