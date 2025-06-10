package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import android.os.Build
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.OnBackInvokedHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.ChangeComment
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WAITING_CONTINUATION_SERIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.state.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWaitingContinuationSeries(
    navController: NavHostController,
    fireBaseMovieViewModel: FireBaseMovieViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listMovies by fireBaseMovieViewModel.movies.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val stateMovies by fireBaseMovieViewModel.movieDownloadStatus.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()

    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    var selectedSerial by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }

    var showTopBar by remember { mutableStateOf(false) }
    val (comment, setComment) = remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        fireBaseMovieViewModel.getMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
        fireBaseMovieViewModel.observeListMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
    }
    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedSerial) {
        selectedSerial?.let { movie ->
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


        if (selectedSerial != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 45.dp)
            ) {

                if (openBottomSheetChange) {
                    MyBottomSheet(
                        onClose = { openBottomSheetChange = false },
                        content = {
                            userData?.let {
                                ChangeComment(
                                    NODE_LIST_WAITING_CONTINUATION_SERIES,
                                    it.nikName,
                                    selectedSerial!!.id,
                                    selectedComment!!,
                                    fireBaseMovieViewModel
                                ) {
                                    openBottomSheetChange = false
                                }
                            }
                        },
                        fraction = 0.5f
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        OnBackInvokedHandler { openBottomSheetChange = false }
                    } else {
                        BackHandler { openBottomSheetChange = false }
                    }
                }

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
                                        if (userData != null) {
                                            fireBaseMovieViewModel.addComment(
                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                                selectedSerial!!.id.toDouble(),
                                                userData!!.nikName,
                                                comment
                                            )
                                            fireBaseMovieViewModel.savingChangeRecord(
                                                context,
                                                userData!!.nikName,
                                                R.string.record_added_comment_to_series,
                                                selectedSerial!!.nameFilm,
                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                                selectedSerial!!.id
                                            )
                                            showToast(context, R.string.comment_added)
                                            setComment("")
                                            openBottomSheetComments = !openBottomSheetComments
                                        }
                                    }
                                )
                            }
                        },
                        fraction = 0.7f
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        OnBackInvokedHandler { openBottomSheetComments = false }
                    } else {
                        BackHandler { openBottomSheetComments = false }
                    }
                }

                DetailsCardSelectedMovie(
                    movie = selectedSerial!!,
                    content = {
                        ShowCommentList(
                            NODE_LIST_WAITING_CONTINUATION_SERIES,
                            selectedSerial!!.id,
                            fireBaseMovieViewModel,
                            onClick = {
                                comment -> selectedComment = comment
                                openBottomSheetChange = true
                            }
                        )
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
                                    fireBaseMovieViewModel.sendingToNewDirectory(
                                        NODE_LIST_WAITING_CONTINUATION_SERIES,
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedSerial!!.id.toDouble()
                                    )
                                    showToast(context, R.string.series_has_been_moved_to_viewed)
                                    fireBaseMovieViewModel.savingChangeRecord(
                                        context,
                                        userData!!.nikName,
                                        R.string.record_series_has_been_moved_to_viewed,
                                        selectedSerial!!.nameFilm,
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedSerial!!.id
                                    )
                                }
                            }
                        )
                    },
                    onClick = {
                        selectedSerial = null
                        showTopBar = !showTopBar
                    }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    OnBackInvokedHandler {
                        selectedSerial = null
                        showTopBar = !showTopBar
                    }
                } else {
                    BackHandler {
                        selectedSerial = null
                        showTopBar = !showTopBar
                    }
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

                            var isVisible by remember { mutableStateOf(true) }
                            LaunchedEffect(isVisible) {
                                if (!isVisible) {
                                    fireBaseMovieViewModel.removeMovie(
                                        NODE_LIST_WAITING_CONTINUATION_SERIES,
                                        movie.id
                                    )
                                    fireBaseMovieViewModel.savingChangeRecord(
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
                                            modifier = Modifier.size(50.dp).padding(end = 10.dp)
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
                is State.Error -> {
                    // TODO: Добавить логику работы при ошибке.
                }
            }
        }
    }
}
