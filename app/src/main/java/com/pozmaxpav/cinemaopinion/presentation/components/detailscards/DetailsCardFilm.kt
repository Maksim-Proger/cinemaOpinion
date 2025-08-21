package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.CustomBoxShowOverlay
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.ShowSharedLists
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImage
import com.pozmaxpav.cinemaopinion.utilits.formatCountries
import com.pozmaxpav.cinemaopinion.utilits.formatGenres
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.toSelectedMovie

@Composable
fun DetailsCardFilm(
    movie: MovieData?,
    onClick: () -> Unit,
    padding: PaddingValues,
    navController: NavHostController,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    movieViewModel: MovieViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userId by mainViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val detailedInformationAboutFilm by apiViewModel.detailedInformationAboutFilm.collectAsState()
    var openSharedLists by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var triggerOnClickPersonalMovie by remember { mutableStateOf(false) }
    var triggerOnClickGeneralMovie by remember { mutableStateOf(false) }

    LaunchedEffect(triggerOnClickGeneralMovie) {
        if (triggerOnClickGeneralMovie) {
            movieViewModel.toastMessage.collect { resId ->
                showToast(context = context, messageId = resId)
                onClick()
            }
        }
    }
    LaunchedEffect(triggerOnClickPersonalMovie) {
        if (triggerOnClickPersonalMovie) {
            personalMovieViewModel.toastMessage.collect { resId ->
                showToast(context = context, messageId = resId)
                onClick()
            }
        }
    }

    LaunchedEffect(Unit) {
        movieViewModel.successfulResult.collect { (dataSource, movie) ->
            userData?.let { user ->
                movieViewModel.savingChangeRecord(
                    context = context,
                    username = user.nikName,
                    stringResourceId = when (dataSource) {
                        NODE_LIST_MOVIES -> { R.string.record_added_movie }
                        NODE_LIST_SERIALS -> { R.string.record_added_series }
                        else -> { R.string.record_added_movie /* <- Заменить это */ }
                    },
                    title = movie.nameFilm,
                    newDataSource = dataSource,
                    entityId = movie.id
                )
            }
        }
    }

    LaunchedEffect(movie?.id) {
        movie?.let { apiViewModel.getSearchMovieById(it.id) }
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(movie?.id) {
        movie?.let { apiViewModel.getInformationMovie(it.id) }
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
                .padding(7.dp),
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
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    movie?.let {
                        WorkerWithImage(
                            movie = it,
                            height = 200.dp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp)
                ) {
                    Text(
                        text = "Название фильма: ${movie?.nameRu ?: stringResource(id = R.string.no_movie_title)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Страна: ${movie?.let { formatCountries(it.countries) }}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    when (movie) {
                        is MovieData.Movie -> {
                            Text(
                                text = "Премьера в России: ${movie.premiereRu}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Жанр: ${formatGenres(movie.genres)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Details(detailedInformationAboutFilm)

                            Spacer(modifier = Modifier.padding(7.dp))
                            ExpandedCard(
                                title = stringResource(R.string.text_for_expandedCard_field),
                                description = info?.description ?: stringResource(R.string.limit_is_over)
                            )

                        }
                        is MovieData.MovieTop -> {
                            Text(
                                text = "Год производства: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг: ${movie.rating}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Details(detailedInformationAboutFilm)

                            Spacer(modifier = Modifier.padding(7.dp))
                            ExpandedCard(
                                title = stringResource(R.string.text_for_expandedCard_field),
                                description = info?.description ?: stringResource(R.string.limit_is_over)
                            )
                        }
                        is MovieData.MovieSearch -> {
                            Text(
                                text = "Год производства: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг Kinopoisk: ${movie.ratingKinopoisk ?: "Нет данных о рейтинге"}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Рейтинг Imdb: ${movie.ratingImdb ?: "Нет данных о рейтинге"}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Details(detailedInformationAboutFilm)

                            Spacer(modifier = Modifier.padding(7.dp))
                            ExpandedCard(
                                title = stringResource(R.string.text_for_expandedCard_field),
                                description = info?.description ?: stringResource(R.string.limit_is_over)
                            )
                        }
                        is MovieData.MovieSearch2 -> {
                            Text(
                                text = "Название на английском: ${movie.nameEn}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(
                                text = "Год производства: ${movie.year}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(7.dp))

                            Details(detailedInformationAboutFilm)

                            Spacer(modifier = Modifier.padding(7.dp))
                            ExpandedCard(
                                title = stringResource(R.string.text_for_expandedCard_field),
                                description = info?.description ?: stringResource(R.string.limit_is_over)
                            )
                        }
                        null -> { // Обработка случая, когда movie == null
                            Text(
                                text = stringResource(id = R.string.movie_is_not_uploaded),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    CustomTextButton(
                        textButton = context.getString(R.string.text_buttons_film_card_to_shared_list),
                        topPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = { openSharedLists = true }
                    )
                    CustomTextButton(
                        textButton = context.getString(R.string.text_buttons_film_card_to_my_list),
                        topPadding = 7.dp,
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = {
                            movie?.toSelectedMovie()?.let { // Преобразуем MovieData в SelectedMovie
                                personalMovieViewModel.addMovieToPersonalList(userId, it)
                            }
                            triggerOnClickPersonalMovie = true
                        }
                    )
                    CustomTextButton(
                        textButton = context.getString(R.string.text_buttons_film_card_to_general_list_movies),
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = {
                            movie?.let { movie ->
                                movieViewModel.saveMovie(
                                    dataSource = NODE_LIST_MOVIES,
                                    selectedMovie = movie.toSelectedMovie()
                                )
                            }
                            triggerOnClickGeneralMovie = true
                        }
                    )
                    CustomTextButton(
                        textButton = context.getString(R.string.text_buttons_film_card_to_general_list_serials),
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = {
                            movie?.let {
                                movieViewModel.saveMovie(
                                    NODE_LIST_SERIALS,
                                    it.toSelectedMovie()
                                )
                            }
                            triggerOnClickGeneralMovie = true
                        }
                    )
                    // region Seasonal Event
//                    CustomTextButton(
//                        textButton = context.getString(R.string.button_add_to_new_year_list),
//                        containerColor = MaterialTheme.colorScheme.secondary,
//                        contentColor = MaterialTheme.colorScheme.onSecondary,
//                        onClickButton = {
//                            userData?.let {
//                                firebaseViewModel.savingChangeRecord(
//                                    context,
//                                    it.nikName,
//                                    R.string.record_added_to_new_year_collection,
//                                    movie?.nameRu.toString()
//                                )
//                            }
//                            movie?.let {
//                                firebaseViewModel.saveMovie(
//                                    NODE_NEW_YEAR_LIST,
//                                    it.toSelectedMovie()
//                                )
//                            }
//                            showToast(context, R.string.movie_has_been_added_to_general_list)
//                            onClick()
//                        } // TODO: Добавить проверку
//                    )
                    // endregion
                }
            }
        }
    }

    if (openSharedLists) {
        CustomBoxShowOverlay(
            onDismiss = { openSharedLists = false },
            paddingVerticalSecondBox = 150.dp,
            paddingHorizontalSecondBox = 36.dp,
            content = {
                ShowSharedLists(
                    navController = navController,
                    userId = userId,
                    addButton = true,
                    selectedMovie = movie?.toSelectedMovie()
                )
            }
        )
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
            modifier = Modifier.fillMaxWidth().padding(7.dp),
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

