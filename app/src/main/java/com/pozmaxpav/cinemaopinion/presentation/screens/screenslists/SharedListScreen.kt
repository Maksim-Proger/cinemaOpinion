package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.alertdialogs.DeleteDialog
import com.example.ui.presentation.components.topappbar.SpecialTopAppBar
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.AddComment
import com.pozmaxpav.cinemaopinion.utilits.ChangeComment
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSharedScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    userName: String,
    listId: String,
    title: String,
    apiViewModel: ApiViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val movies by sharedListsViewModel.movies.collectAsState()
    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val listName by sharedListsViewModel.listName.collectAsState()

    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    var openBottomSheetReviews by remember { mutableStateOf(false) }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
    }
    LaunchedEffect(listId) {
        sharedListsViewModel.getMovies(listId)
        sharedListsViewModel.observeMovies(listId)
        sharedListsViewModel.getListName(listId)
    }
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            if (openBottomSheetChange) {
                CustomBottomSheet(
                    onCloseRequest = { openBottomSheetChange = false }
                ) { onClose ->
                    selectedMovie?.let { movie ->
                        selectedComment?.let { comment ->
                            ChangeComment(
                                sharedListId = listId,
                                userName = userName,
                                selectedMovieId = movie.id,
                                selectedComment = comment,
                                fraction = 0.7f,
                                viewModel = sharedListsViewModel,
                                onClose = onClose
                            )
                        }
                    }
                }
                AdaptiveBackHandler { openBottomSheetChange = false }
            }

            if (openBottomSheetComments) {
                CustomBottomSheet(
                    onCloseRequest = { openBottomSheetComments = false }
                ) { onClose ->
                    AddComment(
                        dataUser = userData,
                        sharedListId = listId,
                        viewModel = sharedListsViewModel,
                        listName = listName,
                        selectedItem = selectedMovie,
                        fraction = 0.7f,
                        context = context,
                        onClick = onClose
                    )
                }
                AdaptiveBackHandler { openBottomSheetComments = false }
            }

            selectedMovie?.let { movie ->
                if (openBottomSheetReviews) {
                    CustomBottomSheet(
                        onCloseRequest = { openBottomSheetReviews = false }
                    ) { onClose ->
                        ShowCommentList(
                            userId = userId,
                            selectedMovieId = movie.id,
                            viewModel = sharedListsViewModel,
                            listId = listId,
                            fraction = 0.7f,
                            onClick = { comment ->
                                selectedComment = comment
                                openBottomSheetChange = true
                            },
                            onClose = onClose
                        )
                    }
                    AdaptiveBackHandler { openBottomSheetReviews = false }
                }
                DetailsCardSelectedMovie(
                    movie = movie,
                    userId = userId,
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
                    onCloseButton = { selectedMovie = null }
                )
                AdaptiveBackHandler { selectedMovie = null }
            }

            if (selectedMovie == null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.statusBars.asPaddingValues())
                            .padding(top = if (isAtTop) TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(movies, key = { it.id }) { movie ->
                            var isVisible by remember(movie.id) { mutableStateOf(true) }
                            var showDeleteDialog by remember(movie.id) { mutableStateOf(false) }

                            LaunchedEffect(isVisible) {
                                if (!isVisible) {
                                    sharedListsViewModel.removeMovie(listId, movie.id)
                                    notificationViewModel.createNotification(
                                        context = context,
                                        entityId = movie.id,
                                        username = userName,
                                        stringResourceId = R.string.record_deleted_the_movie,
                                        title = movie.nameFilm,
                                        sharedListId = listId
                                    )
                                }
                            }

                            if (showDeleteDialog) {
                                DeleteDialog(
                                    entryTitle = movie.nameFilm,
                                    onDismissRequest = { showDeleteDialog = false },
                                    confirmButtonClick = {
                                        showDeleteDialog = false
                                        isVisible = false
                                    },
                                    dismissButtonClick = { showDeleteDialog = false }
                                )
                            }

                            AnimatedVisibility(
                                visible = isVisible,
                                modifier = Modifier.animateItem(),
                                exit = slideOutHorizontally(
                                    targetOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 300)
                                )
                            ) {
                                Card(
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(modifier = Modifier.weight(1f)) {
                                            SelectedMovieItem(
                                                movie = movie,
                                                onClick = { selectedMovie = movie }
                                            )
                                        }
                                        IconButton(
                                            onClick = { showDeleteDialog = true },
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(end = 10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSecondary
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.padding(5.dp))
                        }
                    }
                }
            }
        }

        if (selectedMovie == null) {
            SpecialTopAppBar(
                isAtTop = isAtTop,
                title = title,
                goToBack = { navController.popBackStack() },
                goToHome = { navigateFunction(navController, Route.MainScreen.route) }
            )
        }
    }
}
