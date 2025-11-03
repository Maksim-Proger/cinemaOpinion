package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_SERIALS
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_WATCHED_MOVIES
import com.example.core.utils.state.LoadingState
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.ExpandedCard
import com.example.ui.presentation.components.lottie.CustomLottieAnimation
import com.example.ui.presentation.components.topappbar.SpecialTopAppBar
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.AddComment
import com.pozmaxpav.cinemaopinion.utilits.ChangeComment
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSelectedGeneralMovies(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    movieViewModel: MovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val listMovies by movieViewModel.movies.collectAsState()
    val stateMovie by movieViewModel.movieDownloadStatus.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()

    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
        movieViewModel.getMovies(NODE_LIST_MOVIES)
        movieViewModel.observeListMovies(NODE_LIST_MOVIES)
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (openBottomSheetChange) {
                CustomBottomSheet(
                    onClose = { openBottomSheetChange = false },
                    content = {
                        userData?.let { user ->
                            selectedMovie?.let { movie ->
                                selectedComment?.let { comment ->
                                    ChangeComment(
                                        dataSource = NODE_LIST_MOVIES,
                                        userName = user.nikName,
                                        selectedMovieId = movie.id,
                                        selectedComment = comment,
                                        viewModel = movieViewModel
                                    ) {
                                        openBottomSheetChange = false
                                    }
                                }
                            }
                        }
                    },
                    fraction = 0.5f
                )
                AdaptiveBackHandler { openBottomSheetChange = false }
            }
            if (openBottomSheetComments) {
                CustomBottomSheet(
                    onClose = { openBottomSheetComments = !openBottomSheetComments },
                    content = {
                        AddComment(
                            dataUser = userData,
                            dataSource = NODE_LIST_MOVIES,
                            newDataSource = NODE_LIST_MOVIES,
                            movieViewModel = movieViewModel,
                            selectedItem = selectedMovie,
                            context = context,
                            onClick = { openBottomSheetComments = false }
                        )
                    },
                    fraction = 0.7f
                )
                AdaptiveBackHandler { openBottomSheetComments = false }
            }
            selectedMovie?.let { movie ->
                userData?.let { user ->
                    DetailsCardSelectedMovie(
                        movie = movie,
                        content = {
                            ShowCommentList(
                                dataSource = NODE_LIST_MOVIES,
                                selectedMovieId = movie.id,
                                viewModel = movieViewModel,
                                onClick = { comment ->
                                    selectedComment = comment
                                    openBottomSheetChange = true
                                }
                            )
                        },
                        openDescription = {
                            ExpandedCard(
                                title = stringResource(R.string.text_for_expandedCard_field),
                                description = info?.description
                                    ?: stringResource(R.string.limit_is_over),
                                bottomPadding = 7.dp
                            )
                        },
                        commentButton = {
                            CustomTextButton(
                                textButton = context.getString(R.string.button_leave_comment),
                                bottomPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = {
                                    openBottomSheetComments = !openBottomSheetComments
                                }
                            )
                        },
                        movieTransferButtonToWatchedMoviesList = {
                            CustomTextButton(
                                textButton = context.getString(R.string.button_viewed),
                                topPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = {
                                    movieViewModel.sendingToNewDirectory(
                                        dataSource = NODE_LIST_MOVIES,
                                        directionDataSource = NODE_LIST_WATCHED_MOVIES,
                                        movieId = movie.id.toDouble()
                                    )
                                    showToast(context, R.string.movie_has_been_moved_to_viewed)
                                    movieViewModel.savingChangeRecord(
                                        context = context,
                                        username = user.nikName,
                                        stringResourceId = R.string.record_movie_has_been_moved_to_viewed,
                                        title = movie.nameFilm,
                                        newDataSource = NODE_LIST_WATCHED_MOVIES,
                                        entityId = movie.id
                                    )
                                }
                            )
                        },
                        movieTransferButtonToSerialsList = {
                            CustomTextButton(
                                textButton = context.getString(R.string.button_move_to_series),
                                topPadding = 7.dp,
                                bottomPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = {
                                    movieViewModel.sendingToNewDirectory(
                                        dataSource = NODE_LIST_MOVIES,
                                        directionDataSource = NODE_LIST_SERIALS,
                                        movieId = movie.id.toDouble()
                                    )
                                    showToast(context, R.string.series_has_been_moved)
                                    movieViewModel.savingChangeRecord(
                                        context = context,
                                        username = user.nikName,
                                        stringResourceId = R.string.record_series_has_been_moved_to_series_list,
                                        title = movie.nameFilm,
                                        newDataSource = NODE_LIST_SERIALS,
                                        entityId = movie.id
                                    )
                                }
                            )
                        },
                        onClick = { selectedMovie = null }
                    )
                    AdaptiveBackHandler { selectedMovie = null }
                }
            }
            if (selectedMovie == null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (stateMovie) {
                        is LoadingState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLottieAnimation(
                                    nameFile = "loading_animation.lottie",
                                    modifier = Modifier.scale(0.5f)
                                )
                            }
                        }

                        is LoadingState.Success -> {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(WindowInsets.statusBars.asPaddingValues())
                                    .padding(top = if (isAtTop) TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp),
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                items(listMovies, key = { it.id }) { movie ->

                                    var isVisible by remember { mutableStateOf(true) }

                                    LaunchedEffect(isVisible) {
                                        if (!isVisible) {
                                            movieViewModel.removeMovie(NODE_LIST_MOVIES, movie.id)
                                            movieViewModel.savingChangeRecord(
                                                context,
                                                userData!!.nikName,
                                                R.string.record_deleted_the_movie,
                                                movie.nameFilm,
                                                context.getString(R.string.movie_was_deleted)
                                            )
                                        }
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
                                                    onClick = { isVisible = false },
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

                        is LoadingState.Error -> {
                            // TODO: Добавить логику работы при ошибке.
                        }
                    }
                }
            }

            // region Кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .clickable {
                            navigateFunction(
                                navController,
                                Route.ListWatchedMovies.route
                            )
                        },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = stringResource(R.string.button_viewed_screen),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            // endregion
        }
        SpecialTopAppBar(
            isAtTop = isAtTop,
            title = stringResource(R.string.title_page_movie_list),
            goToBack = { navController.popBackStack() },
            goToHome = { navigateFunction(navController, Route.MainScreen.route) }
        )
    }
}
