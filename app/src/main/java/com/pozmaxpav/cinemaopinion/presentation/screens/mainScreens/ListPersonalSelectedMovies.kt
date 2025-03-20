package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
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
import androidx.compose.runtime.DisposableEffect
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
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.ShowSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListSelectedMovies(
    navController: NavHostController,
    selectedMovieViewModel: SelectedMovieViewModel = hiltViewModel(),
    firebaseViewModel: FirebaseViewModel = hiltViewModel(),
//    viewModelComments: CommentPersonalListViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val stateMovie by firebaseViewModel.movieDownloadStatus.collectAsState()
    val listSelectedMovies by selectedMovieViewModel.selectedMovies.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
//    val listComments by viewModelComments.comments.collectAsState()
    val info by mainViewModel.informationMovie.collectAsState()
    var selectedMovie by remember { mutableStateOf<SelectedMovieModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    val (comment, setComment) = remember { mutableStateOf("") }

    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(userId) {
        selectedMovieViewModel.getListPersonalMovies(userId)
        selectedMovieViewModel.observeListSelectedMovies(userId)
    }

    LaunchedEffect(selectedMovie) {
        if (selectedMovie != null) {
            mainViewModel.getInformationMovie(selectedMovie!!.id)
        }
    }

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
                            Text(text = "Оставьте свой комментарий")
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
//                                    viewModelComments.insertComment(
//                                        selectedNote!!.id.toDouble(),
//                                        comment
//                                    )
//                                    showToast(context, "Комментарий добавлен")
//                                    setComment("")
//                                    openBottomSheetComments = !openBottomSheetComments
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

        if (selectedMovie == null) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp)) {
                IconButton(onClick = { navigateFunction(navController, Route.MainScreen.route) }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.description_icon_back_button),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        if (selectedMovie != null) {
            ShowSelectedMovie(
                movie = selectedMovie!!,
                isGeneralList = false,
                isShowCommentButton = true,
                content = {
//                        ShowListComments(
//                            listComments,
//                            selectedNote!!.id.toDouble()
//                        )
                },
                openDescription = {
                    ExpandedCard(
                        title = "Описание",
                        description = info?.description ?: "К сожалению, суточный лимит закончился"
                    )
                },
                commentButton = {
                    Button(
                        onClick = { openBottomSheetComments = !openBottomSheetComments },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Оставить комментарий",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = { selectedMovie = null }
            )
            BackHandler {
                selectedMovie = null
            }
        } else {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (stateMovie) {
                    is State.Loading -> {
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
                    is State.Success -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            items(listSelectedMovies) { movie ->

                                var isVisible by remember { mutableStateOf(true) }

                                AnimatedVisibility(
                                    visible = isVisible,
                                    exit = slideOutHorizontally(
                                        targetOffsetX = { -it },
                                        animationSpec = tween(durationMillis = 300)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
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
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(0.9f)
                                                ) {
                                                    SelectedMovieItem(
                                                        movie = movie,
                                                        onClick = { selectedMovie = movie }
                                                    )
                                                }

                                                IconButton(
                                                    onClick = {
                                                        isVisible = false
                                                        CoroutineScope(Dispatchers.Main).launch {
                                                            delay(300)
                                                            selectedMovieViewModel
                                                                .deleteSelectedMovie(
                                                                    userId, movie.id.toString()
                                                                )
                                                        }
                                                    }
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
                                }
                                Spacer(Modifier.padding(5.dp))
                            }
                        }
                    }
                    is State.Error -> {}
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            selectedMovieViewModel.onCleared()
        }
    }
}

//@Composable
//private fun ShowListComments(
//    listComments: List<CommentPersonalListModel>,
//    id: Double,
//    viewModelComments: CommentPersonalListViewModel = hiltViewModel()
//) {
//
//    LaunchedEffect(Unit) {
//        viewModelComments.getCommentsList()
//    }
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        if (listComments.isNotEmpty()) {
//            items(listComments) { comment ->
//                Card(
//                    modifier = Modifier
//                        .wrapContentHeight()
//                        .fillMaxWidth()
//                        .padding(vertical = 7.dp),
//                    elevation = CardDefaults.cardElevation(8.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.secondary,
//                        contentColor = MaterialTheme.colorScheme.onSecondary
//                    )
//                ) {
//                    if (id == comment.movieId) {
//                        Column(modifier = Modifier.padding(8.dp)) {
//                            Text(
//                                text = comment.commentText,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = SimpleDateFormat(
//                                    "dd.MM.yyyy HH:mm",
//                                    Locale.getDefault()
//                                ).format(
//                                    Date(comment.timestamp)
//                                )
//
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
