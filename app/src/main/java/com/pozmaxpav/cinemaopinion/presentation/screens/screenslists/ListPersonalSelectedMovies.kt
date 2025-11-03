package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.domain.DomainUserModel
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_PERSONAL_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_SERIALS
import com.example.core.utils.state.LoadingState
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomBoxShowOverlay
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.ExpandedCard
import com.example.ui.presentation.components.lottie.CustomLottieAnimation
import com.example.ui.presentation.components.text.CustomTextFieldForComments
import com.example.ui.presentation.components.topappbar.SpecialTopAppBar
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.OnBackInvokedHandler
import com.pozmaxpav.cinemaopinion.presentation.funs.ShowSharedLists
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.MovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.ChangeComment
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSelectedMovies(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    firebaseViewModel: MovieViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val stateMovie by firebaseViewModel.movieDownloadStatus.collectAsState()
    val listSelectedMovies by personalMovieViewModel.listSelectedMovies.collectAsState()
    val userId by systemViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()

    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    var openSharedLists by remember { mutableStateOf(false) }
    var triggerOnClickSharedMovie by remember { mutableStateOf(false) }

    val isAtTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
    }
    LaunchedEffect(userId) {
        personalMovieViewModel.getMovies(userId)
        personalMovieViewModel.observeMovies(userId)
        userViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }
    LaunchedEffect(triggerOnClickSharedMovie) {
        if (triggerOnClickSharedMovie) {
            sharedListsViewModel.toastMessage.collect { resId ->
                showToast(context = context, messageId = resId)
            }
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
                                        userId = user.id,
                                        userName = user.nikName,
                                        selectedMovieId = movie.id,
                                        selectedComment = comment,
                                        viewModel = personalMovieViewModel
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
                    onClose = { openBottomSheetComments = false },
                    content = {
                        AddComment(
                            personalMovieViewModel = personalMovieViewModel,
                            domainUserModelData = userData,
                            selectedMovie = selectedMovie,
                            context = context,
                            onClick = {
                                openBottomSheetComments = false
                            }
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
                                userId = userId,
                                selectedMovieId = movie.id,
                                viewModel = personalMovieViewModel,
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
                            )
                        },
                        movieTransferButtonToSharedList = {
                            CustomTextButton(
                                textButton = context.getString(R.string.text_buttons_film_card_to_shared_list),
                                topPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = { openSharedLists = true }
                            )
                        },
                        movieTransferButtonToMoviesList = {
                            CustomTextButton(
                                textButton = context.getString(R.string.text_buttons_film_card_to_general_list_movies),
                                topPadding = 7.dp,
                                bottomPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = {
                                    personalMovieViewModel.sendingToNewDirectory(
                                        userId = userId,
                                        dataSource = NODE_LIST_PERSONAL_MOVIES,
                                        directionDataSource = NODE_LIST_MOVIES,
                                        selectedMovieId = movie.id
                                    )
                                    showToast(context, R.string.movie_has_been_moved)
                                    firebaseViewModel.savingChangeRecord(
                                        context,
                                        username = user.nikName,
                                        stringResourceId = R.string.record_added_movie,
                                        title = movie.nameFilm,
                                        newDataSource = NODE_LIST_MOVIES,
                                        entityId = movie.id
                                    )
                                }
                            )
                        },
                        movieTransferButtonToSerialsList = {
                            CustomTextButton(
                                textButton = context.getString(R.string.text_buttons_film_card_to_general_list_serials),
                                bottomPadding = 7.dp,
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                                onClickButton = {
                                    firebaseViewModel.sendingToNewDirectory(
                                        dataSource = NODE_LIST_PERSONAL_MOVIES,
                                        directionDataSource = NODE_LIST_SERIALS,
                                        movieId = movie.id.toDouble()
                                    )
                                    showToast(context, R.string.series_has_been_moved)
                                    firebaseViewModel.savingChangeRecord(
                                        context = context,
                                        username = user.nikName,
                                        stringResourceId = R.string.record_added_series,
                                        title = movie.nameFilm,
                                        newDataSource = NODE_LIST_SERIALS,
                                        entityId = movie.id
                                    )
                                }
                            )
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
                                modifier = Modifier
                                    .fillMaxSize(),
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
                                items(listSelectedMovies, key = { it.id }) { movie ->

                                    var isVisible by remember { mutableStateOf(true) }

                                    LaunchedEffect(isVisible) {
                                        if (!isVisible) {
                                            personalMovieViewModel.deleteMovie(
                                                userId = userId, selectedMovieId = movie.id
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
        }
        if (openSharedLists) {
            CustomBoxShowOverlay(
                onDismiss = { openSharedLists = false },
                paddingVerticalSecondBox = 150.dp,
                paddingHorizontalSecondBox = 36.dp,
                content = {
                    ShowSharedLists(
                        navController = navController,
                        userId = userId,
                        addButton = true,
                        selectedMovie = selectedMovie,
                        onCloseSharedLists = {
                            triggerOnClickSharedMovie = true
                            openSharedLists = false
                            selectedMovie = null
                        }
                    )
                }
            )
        }

        SpecialTopAppBar(
            isAtTop = isAtTop,
            title = stringResource(R.string.title_page_personal_list),
            goToBack = { navController.popBackStack() },
            goToHome = { navigateFunction(navController, Route.MainScreen.route) }
        )
    }

}

@Composable
private fun AddComment(
    personalMovieViewModel: PersonalMovieViewModel,
    domainUserModelData: DomainUserModel?,
    selectedMovie: DomainSelectedMovieModel?,
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
            textButton = stringResource(R.string.button_add),
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            endPadding = 15.dp,
            onClickButton = {
                selectedMovie?.let { movie ->
                    domainUserModelData?.let { user ->
                        personalMovieViewModel.addComment(
                            userId = user.id,
                            selectedMovieId = movie.id,
                            username = user.nikName,
                            textComment = comment
                        )
                        showToast(context, R.string.comment_added)
                        setComment("")
                        onClick()
                    }
                }
            }
        )
    }
}

