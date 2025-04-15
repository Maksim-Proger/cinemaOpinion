package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.state.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    newDataSource: String,
    movieId: Int,
    fireBaseMovieViewModel: FireBaseMovieViewModel = hiltViewModel()
) {

    val movie by fireBaseMovieViewModel.movie.collectAsState()

    LaunchedEffect(movieId) {
        if (newDataSource.isNotEmpty() && movieId != 0) {
            fireBaseMovieViewModel.getMovieById(newDataSource, movieId)
        }
    }

    Scaffold { innerPadding ->
        if (newDataSource == "Фильм удален, страницы нет") {
            TheMovieWasDeleted(innerPadding, navController)
        } else {
            OtherActions(innerPadding, movie, newDataSource, movieId, navController)
        }
    }
}

@Composable
private fun OtherActions(
    innerPadding: PaddingValues,
    movie: DomainSelectedMovieModel?,
    newDataSource: String,
    movieId: Int,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        movie?.let {
            DetailsCardSelectedMovie(
                titleForMovieDetailScreen = elementDirectory(newDataSource),
                movie = it,
                content = {
                    ShowComment(newDataSource, movieId)
                },
                onClick = {
                    navController.navigate(Route.ListOfChangesScreen.route) {
                        popUpTo(Route.MovieDetailScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun TheMovieWasDeleted(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    navController.navigate(Route.ListOfChangesScreen.route) {
                        popUpTo(Route.MovieDetailScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
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
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Фильм удален, страницы нет",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun elementDirectory(newDataSource: String): String {
    return when {
        newDataSource.contains("list_movies") -> "В списке с фильмами"
        newDataSource.contains("list_serials") -> "В списке с сериалами"
        newDataSource.contains("list_watched_movies") -> "В списке просмотренных"
        newDataSource.contains("list_waiting_continuation_series") -> "В листе ожидания"
        else -> ""
    }
}

@Composable
fun ShowComment(
    newDataSource: String,
    movieId: Int,
    firebaseViewModel: FireBaseMovieViewModel = hiltViewModel(),
) {

    val stateComments by firebaseViewModel.commentsDownloadStatus.collectAsState()
    val listComments by firebaseViewModel.comments.collectAsState()

    LaunchedEffect(movieId) {
        firebaseViewModel.getComments(newDataSource, movieId)
        firebaseViewModel.observeComments(newDataSource, movieId)
    }

    when (stateComments) {
        is State.Loading -> {
            CustomLottieAnimation(
                nameFile = "loading_animation.lottie",
                modifier = Modifier.scale(0.5f)
            )
        }

        is State.Success -> {
            LazyColumn {
                items(listComments) { comment ->
                    Card(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = comment.username,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Text(
                                text = comment.commentText,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text =
                                    SimpleDateFormat(
                                        "dd.MM.yyyy HH:mm",
                                        Locale.getDefault()
                                    ).format(
                                        Date(comment.timestamp)
                                    ),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        is State.Error -> {
            // TODO: Добавить логику работы при ошибке.
        }
    }

}