package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WAITING_CONTINUATION_SERIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.state.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWaitingContinuationSeries(
    navController: NavHostController,
    firebaseViewModel: FireBaseMovieViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listMovies by firebaseViewModel.movies.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val stateMovies by firebaseViewModel.movieDownloadStatus.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var showTopBar by remember { mutableStateOf(false) }
    val (comment, setComment) = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        firebaseViewModel.getMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
        firebaseViewModel.observeListMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
    }
    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedNote) {
        selectedNote?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!showTopBar) {
                ClassicTopAppBar(
                    context,
                    titleId = R.string.title_list_waiting_continuation_series,
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }
                )
            }
        }
    ) { innerPadding ->
        if (selectedNote != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 45.dp)
            ) {

                if (openBottomSheetComments) {
                    MyBottomSheet(
                        onClose = {
                            openBottomSheetComments = !openBottomSheetComments
                        },
                        content = {
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
                                        if (userData != null) {
                                            firebaseViewModel.addComment(
                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                                selectedNote!!.id.toDouble(),
                                                userData!!.nikName,
                                                comment
                                            )
                                            firebaseViewModel.savingChangeRecord(
                                                context,
                                                userData!!.nikName,
                                                R.string.record_added_comment_to_series,
                                                selectedNote!!.nameFilm,
                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                                selectedNote!!.id
                                            )
                                            showToast(context, R.string.comment_added)
                                            setComment("")
                                            openBottomSheetComments = !openBottomSheetComments
                                        }
                                    }
                                )
                            )
                        },
                        fraction = 0.7f
                    )
                    BackHandler {
                        openBottomSheetComments = !openBottomSheetComments
                    }
                }

                DetailsCardSelectedMovie(
                    movie = selectedNote!!,
                    content = {
                        ShowCommentWaitingContinuationSeriesList(selectedNote!!.id)
                    },
                    openDescription = {
                        ExpandedCard(
                            title = stringResource(R.string.text_for_expandedCard_field),
                            description = info?.description ?: stringResource(R.string.limit_is_over),
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
                            onClickButton = { openBottomSheetComments = !openBottomSheetComments }
                        )
                    },
                    movieTransferButtonToWatchedMoviesList = {
                        CustomTextButton(
                            textButton = context.getString(R.string.button_viewed),
                            topPadding = 7.dp,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            onClickButton = {
                                if (userData != null) {
                                    firebaseViewModel.sendingToNewDirectory(
                                        NODE_LIST_WAITING_CONTINUATION_SERIES,
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedNote!!.id.toDouble()
                                    )
                                    showToast(context, R.string.series_has_been_moved_to_viewed)
                                    firebaseViewModel.savingChangeRecord(
                                        context,
                                        userData!!.nikName,
                                        R.string.record_series_has_been_moved_to_viewed,
                                        selectedNote!!.nameFilm,
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedNote!!.id
                                    )
                                }
                            }
                        )
                    },
                    onClick = {
                        selectedNote = null
                        showTopBar = !showTopBar
                    }
                )
                BackHandler {
                    selectedNote = null
                    showTopBar = !showTopBar
                }
            }
        } else {
            when (stateMovies) {
                is State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomLottieAnimation(
                            nameFile = "loading_animation.lottie",
                            modifier = Modifier.scale(0.5f)
                        )
                    }
                }
                is State.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(listMovies, key = { it.id }) { movie ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    SelectedMovieItem(
                                        movie = movie,
                                        onClick = { selectedNote = movie },
                                        showTopBar = { showTopBar = !showTopBar }
                                    )
                                }
                            }
                            Spacer(Modifier.padding(5.dp))
                        }
                    }
                }
                is State.Error -> {
                    // TODO: Добавить логику работы при ошибке.
                }
            }
        }
    }
}

@Composable
fun ShowCommentWaitingContinuationSeriesList(
    movieId: Int,
    firebaseViewModel: FireBaseMovieViewModel = hiltViewModel(),
) {
    val stateComments by firebaseViewModel.commentsDownloadStatus.collectAsState()
    val listComments by firebaseViewModel.comments.collectAsState()

    LaunchedEffect(movieId) {
        firebaseViewModel.getComments(NODE_LIST_WAITING_CONTINUATION_SERIES, movieId)
        firebaseViewModel.observeComments(NODE_LIST_WAITING_CONTINUATION_SERIES, movieId)
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
