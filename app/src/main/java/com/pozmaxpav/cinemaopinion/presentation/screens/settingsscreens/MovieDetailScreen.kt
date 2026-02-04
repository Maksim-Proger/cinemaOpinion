package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.CommentBank
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.text.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.utilities.ShowCommentList
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction
import com.pozmaxpav.cinemaopinion.utilities.showToast

@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    listId: String,
    movieId: Int,
    userName: String,
    systemViewModel: SystemViewModel,
    viewModel: SharedListsViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    var openBottomSheetReviews by remember { mutableStateOf(false) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }

    val userId by systemViewModel.userId.collectAsState()
    val movie by viewModel.movie.collectAsState()
    val listName by viewModel.listName.collectAsState()

    LaunchedEffect(movieId) {
        if (listId.isNotEmpty() && movieId != 0) {
            viewModel.getMovieById(listId, movieId)
        }
    }
    LaunchedEffect(listId) {
        viewModel.getListName(listId)
    }

    if (openBottomSheetReviews) {
        CustomBottomSheet(
            onCloseRequest = { openBottomSheetReviews = false }
        ) { onClose ->
            ShowCommentList(
                selectedMovieId = movieId,
                viewModel = viewModel,
                listId = listId,
                fraction = 0.7f,
                onClick = { comment ->
                    selectedComment = comment
                    openBottomSheetChange = true
                },
                onClose = onClose
            )
        }
    }
    if (openBottomSheetComments) {
        CustomBottomSheet(
            onCloseRequest = { openBottomSheetComments = false }
        ) { onClose ->
            AddComment(
                userId = userId,
                movie = movie,
                viewModel = viewModel,
                notificationViewModel = notificationViewModel,
                listId = listId,
                movieId = movieId,
                userName = userName,
                listName = listName,
                fraction = 0.7f,
                context = context,
                onClick = onClose
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        if (movie != null) {
            movie?.let { movie ->
                DetailsCardSelectedMovie(
                    movie = movie,
                    navController = navController,
                    commentButton = {
                        CustomTextButton(
                            textButton = context.getString(R.string.button_leave_comment),
                            imageVector = Icons.Default.AddComment,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            onClickButton = { openBottomSheetComments = !openBottomSheetComments }
                        )
                    },
                    reviews = {
                        CustomTextButton(
                            textButton = context.getString(R.string.button_show_response),
                            imageVector = Icons.Default.CommentBank,
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            onClickButton = { openBottomSheetReviews = !openBottomSheetReviews }
                        )
                    },
                    onCloseButton = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Объект удален",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
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
private fun AddComment(
    userId: String,
    movie: DomainSelectedMovieModel?,
    viewModel: SharedListsViewModel,
    notificationViewModel: NotificationViewModel,
    listId: String,
    movieId: Int,
    userName: String,
    listName: String,
    fraction: Float,
    context: Context,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val (comment, setComment) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(fraction)
            .padding(horizontal = 16.dp)
    ) {
        CustomTextFieldForComments(
            value = comment,
            onValueChange = setComment,
            placeholder = {
                Text(
                    text = stringResource(R.string.button_leave_comment),
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
                modifier = Modifier,
                onClickButton = {
                    viewModel.addComment(
                        listId = listId,
                        movieId = movieId,
                        username = userName,
                        commentUser = comment
                    )

                    movie?.let {
                        notificationViewModel.createNotification(
                            userId = userId,
                            context = context,
                            entityId = movieId,
                            sharedListId = listId,
                            listName = listName,
                            username = userName,
                            stringResourceId = R.string.record_added_comment_to_movie,
                            title = it.nameFilm
                        )
                    }

                    showToast(context, R.string.comment_added)
                    setComment("")
                    onClick()
                }
            )
        }
    }
}
