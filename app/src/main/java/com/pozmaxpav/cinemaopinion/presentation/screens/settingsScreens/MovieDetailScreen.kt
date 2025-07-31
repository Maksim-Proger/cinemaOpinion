package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.state.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    newDataSource: String,
    movieId: Int,
    userName: String,
    movieViewModel: MovieViewModel = hiltViewModel()
) {

    val movie by movieViewModel.movie.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(movieId) {
        if (newDataSource.isNotEmpty() && movieId != 0) {
            movieViewModel.getMovieById(newDataSource, movieId)
        }
    }

    Scaffold { innerPadding ->
        if (newDataSource == stringResource(R.string.movie_was_deleted)) {
            TheMovieWasDeleted(innerPadding, navController)
        } else {
            OtherActions(
                innerPadding,
                movie,
                newDataSource,
                movieId,
                navController,
                movieViewModel,
                userName,
                context
            )
        }
    }
}

@Composable
private fun OtherActions(
    innerPadding: PaddingValues,
    movie: DomainSelectedMovieModel?,
    newDataSource: String,
    movieId: Int,
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    userName: String,
    context: Context,
) {

    var openBottomSheetComments by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        if (openBottomSheetComments) {
            MyBottomSheet(
                onClose = {
                    openBottomSheetComments = !openBottomSheetComments
                },
                content = {
                    AddComment(
                        movie,
                        movieViewModel,
                        newDataSource,
                        movieId,
                        userName,
                        context,
                        onClick = {
                            openBottomSheetComments = false
                        }
                    )
                },
                fraction = 0.7f
            )
        }

        movie?.let { movie ->
            DetailsCardSelectedMovie(
                titleForMovieDetailScreen = elementDirectory(newDataSource),
                movie = movie,
                content = {
                    ShowComment(newDataSource, movieId, movieViewModel)
                },
                commentButton = {
                    CustomTextButton(
                        textButton = context.getString(R.string.placeholder_for_comment_field),
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = { openBottomSheetComments = !openBottomSheetComments }
                    )
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
private fun AddComment(
    movie: DomainSelectedMovieModel?,
    movieViewModel: MovieViewModel,
    newDataSource: String,
    movieId: Int,
    userName: String,
    context: Context,
    onClick: () -> Unit
) {

    val (comment, setComment) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    CustomTextFieldForComments(
        value = comment,
        onValueChange = setComment,
        placeholder = {
            Text(
                text = stringResource(R.string.placeholder_for_comment_field),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        CustomTextButton(
            textButton = "Добавить",
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            endPadding = 15.dp,
            onClickButton = {
                movie?.let { movie ->
                    movieViewModel.addComment(
                        newDataSource,
                        movieId.toDouble(),
                        userName,
                        comment
                    )
                    movieViewModel.savingChangeRecord(
                        context,
                        userName,
                        R.string.record_added_comment_to_movie,
                        movie.nameFilm,
                        newDataSource,
                        movieId
                    )
                    showToast(context, R.string.comment_added)
                    setComment("")
                }
                onClick()
            }
        )
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
                text = stringResource(R.string.movie_was_deleted),
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
    movieViewModel: MovieViewModel,
) {

    val stateComments by movieViewModel.commentsDownloadStatus.collectAsState()
    val listComments by movieViewModel.comments.collectAsState()

    LaunchedEffect(movieId) {
        movieViewModel.getComments(newDataSource, movieId)
        movieViewModel.observeComments(newDataSource, movieId)
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