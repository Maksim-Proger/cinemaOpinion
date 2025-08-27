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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.formatGenres

@Composable
fun DetailsCardSelectedMovie(
    apiViewModel: ApiViewModel = hiltViewModel(),
    titleForMovieDetailScreen: String = "",
    movie: DomainSelectedMovieModel,
    content: @Composable () -> Unit = {},
    openDescription: @Composable () -> Unit = {},
    commentButton: @Composable () -> Unit = {},
    movieTransferButtonToSharedList: @Composable () -> Unit = {},
    movieTransferButtonToWatchedMoviesList: @Composable () -> Unit = {},
    movieTransferButtonToSerialsList: @Composable () -> Unit = {},
    movieTransferButtonToMoviesList: @Composable () -> Unit = {},
    movieTransferButtonToWaitingList: @Composable () -> Unit = {},
    onClick: () -> Unit
) {

    val detailedInformationAboutFilm by apiViewModel.detailedInformationAboutFilm.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(movie.id) { apiViewModel.getSearchMovieById(movie.id) }

    Column(
        modifier = Modifier.wrapContentSize().padding(top = 10.dp)
    ) {
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
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
                Spacer(modifier = Modifier.height(7.dp))

                Details(detailedInformationAboutFilm)

                Spacer(modifier = Modifier.height(7.dp))
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Жанр: ${detailedInformationAboutFilm?.let { formatGenres(it.genres) }}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.padding(7.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    movieTransferButtonToSharedList()
                    movieTransferButtonToWatchedMoviesList()
                    movieTransferButtonToWaitingList()
                    movieTransferButtonToMoviesList()
                    movieTransferButtonToSerialsList()
                    commentButton()
                    openDescription()
                }

                Column(
                    modifier = Modifier
                        .height(500.dp)
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun Details(detailedInformationAboutFilm: MovieData.MovieSearch?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Кинопоиск\n${detailedInformationAboutFilm?.ratingKinopoisk ?: "Н/Д"}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "IMDb\n${detailedInformationAboutFilm?.ratingImdb ?: "Н/Д"}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Длительность\n${detailedInformationAboutFilm?.filmLength ?: "Н/Д"} мин.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

}