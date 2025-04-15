package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie

@Composable
fun DetailsCardSelectedMovie(
    titleForMovieDetailScreen: String = "",
    movie: DomainSelectedMovieModel,
    content: @Composable () -> Unit = {},
    openDescription: @Composable () -> Unit = {},
    commentButton: @Composable () -> Unit = {},
    movieTransferButtonToWatchedMoviesList: @Composable () -> Unit = {},
    movieTransferButtonToSerialsList: @Composable () -> Unit = {},
    movieTransferButtonToMoviesList: @Composable () -> Unit = {},
    movieTransferButtonToWaitingList: @Composable () -> Unit = {},
    onClick: () -> Unit
) {

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.wrapContentSize()) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 7.dp),
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
                Spacer(Modifier.padding(start = 16.dp))
                Text(
                    text = titleForMovieDetailScreen,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    movieTransferButtonToWatchedMoviesList()
                    movieTransferButtonToWaitingList()
                    movieTransferButtonToMoviesList()
                    movieTransferButtonToSerialsList()
                    commentButton()
                    openDescription()
                }

                Column(
                    modifier = Modifier
                        .height(500.dp) // TODO: Подумать на параметром высоты.
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}