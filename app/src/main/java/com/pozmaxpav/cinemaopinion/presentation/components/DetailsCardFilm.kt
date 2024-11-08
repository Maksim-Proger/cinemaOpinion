package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage
import com.pozmaxpav.cinemaopinion.utilits.formatCountries
import com.pozmaxpav.cinemaopinion.utilits.formatGenres
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.toSelectedMovie

@Composable
fun DetailsCardFilm(
    addToPersonalList: String,
    errorToast: String,
    addToGeneralList: String,
    movie: MovieData,
    onClick: () -> Unit,
    padding: PaddingValues,
    user: String,
    viewModel: SelectedMovieViewModel = hiltViewModel(),
    viewModelFirebase: FirebaseViewModel = hiltViewModel(),
    viewModelMain: MainViewModel = hiltViewModel()
) {
    val statusExist by viewModel.status.collectAsState()
    val context = LocalContext.current

    val info by viewModelMain.informationMovie.collectAsState()
    // Выполняем запрос к API только при изменении `movie.id`
    // Это нужно, чтобы не было многократного запроса к Api
    LaunchedEffect(movie.id) {
        viewModelMain.getInformationMovie(movie.id)
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(padding)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onClick() }
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
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WorkerWithImage(movie, 200.dp)
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Название фильма: ${movie.nameRu ?: stringResource(id = R.string.no_movie_title)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Страна: ${formatCountries(movie.countries)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    when(movie) {
                        is MovieData.Movie -> {
                            Text(
                                text = "Премьера в России: ${movie.premiereRu}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Жанр: ${formatGenres(movie.genres)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.padding(15.dp))
                            ExpandedCard(
                                title = "Описание",
                                description = info?.description ?: "К сожалению, суточный лимит закончился"
                            )

                        }
                        is MovieData.MovieTop -> {
                            Text(
                                text = "Год производства: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг: ${movie.rating}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.padding(15.dp))
                            ExpandedCard(
                                title = "Описание",
                                description = info?.description ?: "К сожалению, суточный лимит закончился"
                            )
                        }
                        is MovieData.MovieSearch -> {
                            Text(
                                text = "Год производства: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг Kinopoisk: ${movie.ratingKinopoisk ?: "Нет данных о рейтинге"}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг Imdb: ${movie.ratingImdb ?: "Нет данных о рейтинге"}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.padding(15.dp))
                            ExpandedCard(
                                title = "Описание",
                                description = info?.description ?: "К сожалению, суточный лимит закончился"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            // Преобразуем MovieData в SelectedMovie
                            val selectedMovie = movie.toSelectedMovie()
                            viewModel.addSelectedMovie(selectedMovie)

                            if (statusExist == "error") {
                                showToast(context, errorToast)
                            } else showToast(context, addToPersonalList)

                            onClick()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.text_buttons_film_card_to_my_list),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        onClick = {
                            viewModelFirebase.savingChangeRecord(
                                user,
                                "добавил(а) фильм: ${movie.nameRu}"
                            )
                            viewModelFirebase.saveMovie(movie.toSelectedMovie())
                            showToast(context, addToGeneralList)
                            onClick()
                        },
                    ) {
                        Text(
                            text = stringResource(R.string.text_buttons_film_card_to_general_list),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

