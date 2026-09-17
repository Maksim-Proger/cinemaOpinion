package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
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
import com.example.ui.presentation.components.ActionButton
import com.example.ui.presentation.components.InlineBottomSheet
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilities.AddComment
import com.pozmaxpav.cinemaopinion.utilities.ChangeComment
import com.pozmaxpav.cinemaopinion.utilities.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilities.navigateFunction

@Composable
fun NotificationDetailScreen(
    navController: NavHostController,
    listId: String,
    movieId: Int,
    userName: String,
    dataSource: String,
    systemViewModel: SystemViewModel,
    userViewModel: UserViewModel = hiltViewModel(),
    viewModel: SharedListsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var openBottomSheetShowComments by remember { mutableStateOf(false) }
    var openBottomSheetAddComments by remember { mutableStateOf(false) }
    var openBottomSheetChangeComments by remember { mutableStateOf(false) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }

    val userId by systemViewModel.userId.collectAsState()
    val movie by viewModel.movie.collectAsState()
    val listName by viewModel.listName.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    fun hideKeyboard() {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
    }

    fun closeCommentList() {
        openBottomSheetShowComments = false
    }

    fun closeAddComment() {
        hideKeyboard()
        openBottomSheetAddComments = false
    }

    fun closeChangeComment() {
        hideKeyboard()
        openBottomSheetChangeComments = false
    }

    LaunchedEffect(movieId) {
        if (listId.isNotEmpty() && movieId != 0) {
            viewModel.getMovieById(listId, movieId)
        }
    }
    LaunchedEffect(listId) {
        viewModel.getListName(listId)
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            if (movie != null) {
                movie?.let { movie ->
                    DetailsCardSelectedMovie(
                        movie = movie,
                        navController = navController,
                        needSharedListButton = false,
                        needSkipButton = false,
                        needFavoritesButton = false,
                        reviews = { openBottomSheetShowComments = true },
                        onCloseButton = { navigateFunction(navController, Route.MainScreen.route) }
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

        InlineBottomSheet(
            visible = openBottomSheetShowComments,
            onDismissRequest = { openBottomSheetShowComments = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            ShowCommentList(
                userId = userId,
                selectedMovieId = movieId,
                viewModel = viewModel,
                sharedListId = listId,
                dataSource = dataSource,
                fraction = 0.7f,
                addCommentButton = {
                    val addComment = stringResource(R.string.button_leave_comment)
                    ActionButton(
                        icon = Icons.Default.AddComment,
                        label = addComment,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                        onClick = { openBottomSheetAddComments = true }
                    )
                },
                onClick = { comment ->
                    if (userName == comment.username) {
                        selectedComment = comment
                        openBottomSheetChangeComments = true
                    }
                },
                onClickCloseButton = { closeCommentList() }
            )
        }

        InlineBottomSheet(
            visible = openBottomSheetAddComments,
            onDismissRequest = { openBottomSheetAddComments = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            AddComment(
                dataUser = userData,
                sharedListId = listId,
                dataSource = dataSource,
                viewModel = viewModel,
                listName = listName,
                selectedItem = movie,
                fraction = 0.7f,
                context = context,
                onClick = { closeAddComment() },
                onClickCloseButton = { closeAddComment() }
            )
        }

        InlineBottomSheet(
            visible = openBottomSheetChangeComments,
            onDismissRequest = { openBottomSheetChangeComments = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            selectedComment?.let { comment ->
                ChangeComment(
                    userId = userId,
                    sharedListId = listId,
                    userName = userName,
                    selectedMovieId = movieId,
                    selectedComment = comment,
                    fraction = 0.7f,
                    viewModel = viewModel,
                    onClickCloseButton = { closeChangeComment() }
                )
            }
        }

    }

}
