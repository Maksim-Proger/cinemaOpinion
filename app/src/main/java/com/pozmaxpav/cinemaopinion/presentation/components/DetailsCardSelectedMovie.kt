package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie

@Composable
fun ShowSelectedMovie(
    movie: SelectedMovie,
    buttonVisibility: Boolean,
    content: @Composable () -> Unit = {},
    openDescription: @Composable () -> Unit = {},
    commentButton: @Composable () -> Unit = {},
    movieTransferButton: @Composable () -> Unit = {},
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onClick() },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxSize()
                    .padding(horizontal = 7.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    WorkerWithImageSelectedMovie(
                        movie = movie,
                        height = 200.dp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = movie.nameFilm,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        movieTransferButton()
                        Spacer(Modifier.padding(horizontal = 4.dp))
                        if (buttonVisibility) {
                            commentButton()
                        }
                    }
                    Spacer(Modifier.padding(vertical = 7.dp))
                    openDescription()
                }

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}