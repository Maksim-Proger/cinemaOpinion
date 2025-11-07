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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_WAITING_CONTINUATION_SERIES
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
fun ListWaitingContinuationSeries(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    movieViewModel: MovieViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val listMovies by movieViewModel.movies.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val stateMovies by movieViewModel.movieDownloadStatus.collectAsState()
    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()

    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    var selectedSerial by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    var showTopBar by remember { mutableStateOf(false) }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
        movieViewModel.getMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
        movieViewModel.observeListMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
    }
    LaunchedEffect(userId) {
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedSerial) {
        selectedSerial?.let { movie ->
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
                            selectedSerial?.let { serial ->
                                selectedComment?.let { comment ->
                                    ChangeComment(
                                        dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
                                        userName = user.nikName,
                                        selectedMovieId = serial.id,
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
                    onClose = {
                        openBottomSheetComments = !openBottomSheetComments
                    },
                    content = {
                        AddComment(
                            dataUser = userData,
                            dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
                            newDataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
                            movieViewModel = movieViewModel,
                            selectedItem = selectedSerial,
                            context = context,
                            onClick = { openBottomSheetComments = false }
                        )
                    },
                    fraction = 0.7f
                )
                AdaptiveBackHandler { openBottomSheetComments = false }
            }

            selectedSerial?.let { serial ->
                userData?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 45.dp)
                    ) {
                        DetailsCardSelectedMovie(
                            movie = serial,
                            content = {
                                ShowCommentList(
                                    dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
                                    selectedMovieId = serial.id,
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
                                    topPadding = 7.dp,
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
                                            dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
                                            directionDataSource = NODE_LIST_WATCHED_MOVIES,
                                            movieId = serial.id.toDouble()
                                        )
                                        showToast(context, R.string.series_has_been_moved_to_viewed)
                                        movieViewModel.savingChangeRecord(
                                            context = context,
                                            username = user.nikName,
                                            stringResourceId = R.string.record_series_has_been_moved_to_viewed,
                                            title = serial.nameFilm,
                                            newDataSource = NODE_LIST_WATCHED_MOVIES,
                                            entityId = serial.id
                                        )
                                    }
                                )
                            },
                            onClick = {
                                selectedSerial = null
                                showTopBar = !showTopBar
                            }
                        )
                        AdaptiveBackHandler {
                            selectedSerial = null
                            showTopBar = !showTopBar
                        }
                    }
                }
            }

            if (selectedSerial == null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (stateMovies) {
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
                                            movieViewModel.removeMovie(
                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                                movie.id
                                            )
                                            movieViewModel.savingChangeRecord(
                                                context,
                                                userData!!.nikName,
                                                R.string.record_deleted_the_movie,
                                                movie.nameFilm,
                                                "Фильм удален, страницы нет",
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
                                                        onClick = { selectedSerial = movie },
                                                        showTopBar = { showTopBar = !showTopBar }
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
        }

        if (selectedSerial == null) {
            SpecialTopAppBar(
                isAtTop = isAtTop,
                title = stringResource(R.string.title_list_waiting_continuation_series),
                goToBack = { navController.popBackStack() },
                goToHome = { navigateFunction(navController, Route.MainScreen.route) }
            )
        }
    }
}
