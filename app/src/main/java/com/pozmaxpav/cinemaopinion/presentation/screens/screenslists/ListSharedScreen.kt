package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.OnBackInvokedHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.UserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.navigateFunctionClearAllScreens
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSharedScreen(
    navController: NavHostController,
    listId: String,
    title: String,
    apiViewModel: ApiViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sharedListsViewModel: SharedListsViewModel = hiltViewModel()
) {
    val movies by sharedListsViewModel.movies.collectAsState()
    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    val userId by mainViewModel.userId.collectAsState()
    val userData by userViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(listId) { sharedListsViewModel.getMovies(listId) }
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }
    LaunchedEffect(userId) { userViewModel.getUserData(userId) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleString = title,
                scrollBehavior = scrollBehavior,
                onTransitionAction = {
                    navigateFunctionClearAllScreens(navController, Route.MainScreen.route)
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            if (openBottomSheetChange) {
                MyBottomSheet(
                    onClose = { openBottomSheetChange = false },
                    content = { /* TODO: Добавить действие */ },
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
                    onClose = { openBottomSheetComments = false },
                    content = {
                        AddComment(
                            userData = userData,
                            viewModel = sharedListsViewModel,
                            selectedMovie = selectedMovie,
                            context = context,
                            listId = listId,
                            onClick = { openBottomSheetComments = false }
                        )
                    },
                    fraction = 0.7f
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    OnBackInvokedHandler { openBottomSheetComments = false }
                } else {
                    BackHandler { openBottomSheetComments = false }
                }
            }

            selectedMovie?.let { movie ->
                DetailsCardSelectedMovie(
                    movie = movie,
                    content = {
                        ShowCommentList(
                            userId = userId,
                            selectedMovieId = movie.id,
                            viewModel = sharedListsViewModel,
                            listId = listId,
                            onClick = { }
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
                            bottomPadding = 7.dp,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                            onClickButton = { openBottomSheetComments = !openBottomSheetComments }
                        )
                    },
                    onClick = { selectedMovie = null }
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    OnBackInvokedHandler { selectedMovie = null }
                } else { BackHandler { selectedMovie = null } }
            }

            if (selectedMovie == null) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(movies, key = { it.id }) { movie ->

                        var isVisible by remember { mutableStateOf(true) }

                        LaunchedEffect(isVisible) {
                            if (!isVisible) {
                                // TODO: Добавить метод удаления
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
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
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
        }
    }
}

@Composable
private fun AddComment(
    userData: User?,
    viewModel: SharedListsViewModel,
    selectedMovie: DomainSelectedMovieModel?,
    context: Context,
    listId: String,
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
            textButton = "Добавить",
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            endPadding = 15.dp,
            onClickButton = {
                userData?.let { user ->
                    selectedMovie?.let { movie ->
                        viewModel.addComment(
                            listId = listId,
                            movieId = movie.id,
                            username = user.nikName,
                            commentUser = comment
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