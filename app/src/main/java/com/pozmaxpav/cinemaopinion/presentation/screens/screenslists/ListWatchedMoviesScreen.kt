package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import android.os.Build
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
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.OnBackInvokedHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.ChangeComment
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.state.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWatchedMovies(
    navController: NavHostController,
    fireBaseMovieViewModel: FireBaseMovieViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val listMovies by fireBaseMovieViewModel.movies.collectAsState()
    val stateMovies by fireBaseMovieViewModel.movieDownloadStatus.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()

    var showTopBar by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }

    val (comment, setComment) = remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        fireBaseMovieViewModel.getMovies(NODE_LIST_WATCHED_MOVIES)
    }
    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!showTopBar) {
                ClassicTopAppBar(
                    context,
                    titleId = R.string.title_listWatched_movies,
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }
                )
            }
        }
    ) { innerPadding ->

        if (openBottomSheetChange) {
            MyBottomSheet(
                onClose = { openBottomSheetChange = false },
                content = {
                    userData?.let {
                        ChangeComment(
                            NODE_LIST_WATCHED_MOVIES,
                            it.nikName,
                            selectedMovie!!.id,
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
                                if (userData != null) {
                                    fireBaseMovieViewModel.addComment(
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedMovie!!.id.toDouble(),
                                        userData!!.nikName,
                                        comment
                                    )
                                    fireBaseMovieViewModel.savingChangeRecord(
                                        context,
                                        userData!!.nikName,
                                        R.string.record_added_comment_to_movie_in_the_viewed,
                                        selectedMovie!!.nameFilm,
                                        NODE_LIST_WATCHED_MOVIES,
                                        selectedMovie!!.id
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

        if (selectedMovie != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 45.dp)
            ) {
                DetailsCardSelectedMovie(
                    movie = selectedMovie!!,
                    content = {
                        ShowCommentList(
                            NODE_LIST_WATCHED_MOVIES,
                            selectedMovie!!.id,
                            fireBaseMovieViewModel,
                            onClick = {
                                comment -> selectedComment = comment
                                openBottomSheetChange = true
                            }
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
                    onClick = {
                        selectedMovie = null
                        showTopBar = !showTopBar
                    }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    OnBackInvokedHandler {
                        selectedMovie = null
                        showTopBar = !showTopBar
                    }
                } else {
                    BackHandler {
                        selectedMovie = null
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
                            Row(
                                modifier = Modifier
                                    .animateItem()
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
                                        onClick = { selectedMovie = movie },
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
