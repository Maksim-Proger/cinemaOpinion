package com.pozmaxpav.cinemaopinion.utilits

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.core.domain.DomainUserModel
import com.example.core.utils.state.LoadingState
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.lottie.CustomLottieAnimation
import com.example.ui.presentation.components.text.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Country
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Genre
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main.MainScreenState
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun WorkerWithImage(
    movie: MovieData,
    height: Dp
) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = movie.nameRu ?: stringResource(id = R.string.no_movie_title),
        modifier = Modifier.height(height),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun WorkerWithImageSelectedMovie(
    movie: DomainSelectedMovieModel,
    height: Dp
) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = movie.nameFilm,
        modifier = Modifier.height(height),
        contentScale = ContentScale.Fit
    )
}

fun formatGenres(genres: List<Genre>): String {
    return genres.joinToString(separator = ", ") {
        it.genre
    }
}

fun formatCountries(country: List<Country>): String {
    return country.joinToString(separator = ", ") {
        it.country
    }
}

fun formatMonth(month: String): String {
    return try {
        Month.of(month.toInt())
            .name
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
    } catch (e: DateTimeException) {
        "Invalid month"
    } catch (e: NumberFormatException) {
        "Invalid month"
    }
}

fun formatDate(date: String): String {
    val formatterInput = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val dateTime = LocalDateTime.parse(date, formatterInput)
    return dateTime.format(formatterOutput)
}

fun deletingOldRecords(timestamp: Long): Boolean {
    val currentTimeMillis = System.currentTimeMillis() // Получаем текущую дату
    val differenceInMillis =
        abs(currentTimeMillis - timestamp) // Рассчитываем разницу между текущим временем и входящей датой
    val daysDifference = differenceInMillis / (1000 * 60 * 60 * 24) // Конвертируем разницу в дни
    return daysDifference > 7
}

// Кастуем объект MovieData в SelectedMovie
fun MovieData.toSelectedMovie(): DomainSelectedMovieModel {
    return when (this) {
        is MovieData.Movie -> DomainSelectedMovieModel(
            id = this.kinopoiskId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )

        is MovieData.MovieTop -> DomainSelectedMovieModel(
            id = this.filmId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )

        is MovieData.MovieSearch -> DomainSelectedMovieModel(
            id = this.kinopoiskId,
            nameFilm = this.nameRu ?: "Нет названия",
            posterUrl = this.posterUrl
        )

        is MovieData.MovieSearch2 -> DomainSelectedMovieModel(
            id = this.filmId,
            nameFilm = this.nameRu ?: "Нет названия",
            posterUrl = this.posterUrl
        )
    }
}

fun showToast(context: Context, messageId: Int) {
    val message = context.getString(messageId)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun navigateFunction(navController: NavHostController, route: String) {

    navController.navigate(route) {
        launchSingleTop = true
        restoreState = true
        //popUpTo(navController.graph.startDestinationId) { saveState = true }
    }

//    navController.navigate(route) {
//        // Открывает новый экран поверх текущего (стандартное поведение)
//        launchSingleTop = true // предотвращает дубликаты, если экран уже наверху
//    }

}

fun navigateFunctionClearAllScreens(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

fun parsYearsToString(range: ClosedFloatingPointRange<Float>): List<String> {
    val yearsList = listOf(
        range.start.toInt().toString(),
        range.endInclusive.toInt().toString()
    )
    return yearsList
}

fun formatTextWithUnderscores(text: String): String {
    return text
        .replace("[\\s\\p{P}]".toRegex(), "_")
        .replace("_+".toRegex(), "_")
        .removePrefix("_")
        .removeSuffix("_")
        .lowercase()
}

fun simpleTransliterate(text: String): String {
    val simpleMap = mapOf(
        'а' to "a", 'б' to "b", 'в' to "v", 'г' to "g", 'д' to "d",
        'е' to "e", 'ё' to "e", 'ж' to "j", 'з' to "z", 'и' to "i",
        'й' to "y", 'к' to "k", 'л' to "l", 'м' to "m", 'н' to "n",
        'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s", 'т' to "t",
        'у' to "u", 'ф' to "f", 'х' to "h", 'ц' to "c", 'ч' to "ch",
        'ш' to "sh", 'щ' to "sh", 'ъ' to "", 'ы' to "y", 'ь' to "",
        'э' to "e", 'ю' to "ju", 'я' to "ja"
    )

    return text.lowercase().map { char ->
        simpleMap[char] ?: char.toString()
    }.joinToString("")
}

@Composable
fun AddComment(
    dataUser: DomainUserModel?,
    sharedListId: String = "",
    viewModel: ViewModel,
    selectedItem: DomainSelectedMovieModel?,
    context: Context,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (comment, setComment) = remember { mutableStateOf("") }

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
            textButton = stringResource(R.string.button_add),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            endPadding = 15.dp,
            onClickButton = {
                when (viewModel) {
                    is PersonalMovieViewModel -> {
                        dataUser?.let { user ->
                            selectedItem?.let { movie ->
                                viewModel.addComment(
                                    userId = user.id,
                                    username = user.nikName,
                                    selectedMovieId = movie.id,
                                    textComment = comment
                                )
                                showToast(context, R.string.comment_added)
                                setComment("")
                                onClick()
                            }
                        }
                    }

                    is SharedListsViewModel -> {
                        dataUser?.let { user ->
                            selectedItem?.let { movie ->
                                viewModel.addComment(
                                    listId = sharedListId,
                                    movieId = movie.id,
                                    username = user.nikName,
                                    commentUser = comment
                                )
                                notificationViewModel.createNotification(
                                    context = context,
                                    entityId = movie.id,
                                    sharedListId = sharedListId,
                                    username = user.nikName,
                                    stringResourceId = R.string.record_added_comment_to_movie,
                                    title = movie.nameFilm
                                )
                                showToast(context, R.string.comment_added)
                                setComment("")
                                onClick()
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ShowCommentList(
    userId: String = "",
    selectedMovieId: Int,
    viewModel: ViewModel,
    listId: String = "",
    onClick: (DomainCommentModel) -> Unit,
) {
    when (viewModel) {
        is PersonalMovieViewModel -> {
            val stateComments by viewModel.commentsDownloadStatus.collectAsState()
            val listComments by viewModel.listComments.collectAsState()

            LaunchedEffect(userId) {
                if (userId.isNotEmpty()) {
                    viewModel.getComments(userId, selectedMovieId)
                    viewModel.observeComments(userId, selectedMovieId)
                }
            }

            when (stateComments) {
                is LoadingState.Loading -> {
                    CustomLottieAnimation(
                        nameFile = "loading_animation.lottie",
                        modifier = Modifier.scale(0.5f)
                    )
                }
                is LoadingState.Success -> {
                    LazyColumn {
                        items(listComments) { comment ->
                            Card(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                                    .padding(vertical = 7.dp)
                                    .clickable { onClick(comment) },
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
                                            text = SimpleDateFormat(
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
                is LoadingState.Error -> {
                    Text(
                        text = "При загрузке произошла ошибка.",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        is SharedListsViewModel -> {
            val stateComments by viewModel.commentsDownloadStatus.collectAsState()
            val comments by viewModel.comments.collectAsState()

            LaunchedEffect(userId) {
                if (userId.isNotEmpty()) {
                    viewModel.getComments(listId, selectedMovieId)
                }
            }

            when (stateComments) {
                is LoadingState.Loading -> {
                    CustomLottieAnimation(
                        nameFile = "loading_animation.lottie",
                        modifier = Modifier.scale(0.5f)
                    )
                }
                is LoadingState.Success -> {
                    LazyColumn {
                        items(comments, key = { it.commentId }) { comment ->
                            Card(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                                    .padding(vertical = 7.dp)
                                    .clickable { onClick(comment) },
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
                                            text = SimpleDateFormat(
                                                "dd.MM.yyyy HH:mm", Locale.getDefault()
                                            ).format(Date(comment.timestamp)),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is LoadingState.Error -> {
                    Text(
                        text = "При загрузке произошла ошибка.",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ChangeComment(
    userId: String = "",
    userName: String,
    selectedMovieId: Int,
    selectedComment: DomainCommentModel,
    viewModel: ViewModel,
    onClick: () -> Unit
) {
    when (viewModel) {
//        is MovieViewModel -> {
//            val (comment, setComment) = remember { mutableStateOf("") }
//            val keyboardController = LocalSoftwareKeyboardController.current
//            val focusManager = LocalFocusManager.current
//
//            LaunchedEffect(Unit) {
//                setComment(selectedComment.commentText)
//            }
//
//            CustomTextFieldForComments(
//                value = comment,
//                onValueChange = setComment,
//                label = {
//                    Text(
//                        text = stringResource(R.string.text_for_field_change_comment),
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                },
//                placeholder = {
//                    Text(
//                        text = stringResource(R.string.placeholder_for_comment_field),
//                        style = MaterialTheme.typography.bodyMedium
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.outline
//                    )
//                },
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        keyboardController?.hide()
//                        focusManager.clearFocus()
//                    }
//                )
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                CustomTextButton(
//                    textButton = stringResource(R.string.button_save),
//                    containerColor = MaterialTheme.colorScheme.secondary,
//                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                    endPadding = 15.dp,
//                    onClickButton = {
//                        viewModel.updateComment(
//                            dataSource = dataSource,
//                            userName = userName,
//                            selectedMovieId = selectedMovieId,
//                            commentId = selectedComment.commentId,
//                            newCommentText = comment
//                        )
//                        onClick()
//                    }
//                )
//            }
//        }
        is PersonalMovieViewModel -> {
            val (comment, setComment) = remember { mutableStateOf("") }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            LaunchedEffect(Unit) {
                setComment(selectedComment.commentText)
            }

            CustomTextFieldForComments(
                value = comment,
                onValueChange = setComment,
                label = {
                    Text(
                        text = stringResource(R.string.text_for_field_change_comment),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
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
                    textButton = stringResource(R.string.button_save),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    endPadding = 15.dp,
                    onClickButton = {
                        viewModel.updateComment(
                            userId = userId,
                            userName = userName,
                            selectedMovieId = selectedMovieId,
                            commentId = selectedComment.commentId,
                            newCommentText = comment
                        )
                        onClick()
                    }
                )
            }
        }
    }
}

@Composable
fun SendRequestAdvancedSearch(
    state: MainScreenState,
    apiViewModel: ApiViewModel
) {
    if (state.sendRequestCompleted.value) {
        state.requestBody.value.let { compositeRequest ->
            apiViewModel.searchFilmsByFilters(
                compositeRequest.type,
                compositeRequest.keyword,
                compositeRequest.countries,
                compositeRequest.genres,
                compositeRequest.ratingFrom,
                compositeRequest.yearFrom,
                compositeRequest.yearTo,
                state.currentPage.intValue
            )
            state.sendRequestCompleted.value = false
        }
    }
}

@Composable
fun SendSelectedDate(
    state: MainScreenState,
    apiViewModel: ApiViewModel
) {
    if (state.dateSelectionComplete.value) {
        state.selectedDate.value?.let {
            apiViewModel.fetchPremiersMovies(it.first, formatMonth(it.second))
            state.dateSelectionComplete.value = false
        }
    }
}

