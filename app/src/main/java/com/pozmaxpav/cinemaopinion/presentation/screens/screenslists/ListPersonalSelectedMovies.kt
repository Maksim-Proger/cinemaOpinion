package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_PERSONAL_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ListSelectedMovies(
    navController: NavHostController,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    firebaseViewModel: FireBaseMovieViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel()
) {

    val stateMovie by firebaseViewModel.movieDownloadStatus.collectAsState()
    val listSelectedMovies by personalMovieViewModel.selectedMovies.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    val (comment, setComment) = remember { mutableStateOf("") }

    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(userId) {
        personalMovieViewModel.getListPersonalMovies(userId)
        personalMovieViewModel.observeListSelectedMovies(userId)
        auxiliaryUserViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedMovie) {
        selectedMovie?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
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
                                personalMovieViewModel.addCommentToPersonalList(
                                    userId,
                                    selectedMovie!!.id,
                                    userData!!.nikName,
                                    comment
                                )
                                showToast(context, R.string.comment_added)
                                setComment("")
                                openBottomSheetComments = !openBottomSheetComments
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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 7.dp)) {
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
            DetailsCardSelectedMovie(
                movie = selectedMovie!!,
                content = {
                    ShowListComments(
                        userId,
                        selectedMovie!!.id
                    )
                },
                openDescription = {
                    ExpandedCard(
                        title = stringResource(R.string.text_for_expandedCard_field),
                        description = info?.description ?: stringResource(R.string.limit_is_over),
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
                            firebaseViewModel.sendingToNewDirectory(
                                NODE_LIST_PERSONAL_MOVIES,
                                NODE_LIST_MOVIES,
                                selectedMovie!!.id.toDouble()
                            )
                            showToast(context, R.string.movie_has_been_moved)
                            firebaseViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_added_movie,
                                selectedMovie!!.nameFilm
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
                                NODE_LIST_PERSONAL_MOVIES,
                                NODE_LIST_SERIALS,
                                selectedMovie!!.id.toDouble()
                            )
                            showToast(context, R.string.series_has_been_moved)
                            firebaseViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_added_series,
                                selectedMovie!!.nameFilm
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
                onClick = {
                    selectedMovie = null
                }
            )
            BackHandler {
                selectedMovie = null
            }
        } else {
            Column(
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
                            items(listSelectedMovies, key = { it.id }) { movie ->

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
                                                            personalMovieViewModel
                                                                .deleteSelectedMovie(
                                                                    userId, movie.id
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
                    is State.Error -> {
                        // TODO: Добавить логику работы при ошибке.
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowListComments(
    userId: String,
    selectedMovieId: Int,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel()
) {

    val listComments by personalMovieViewModel.listComments.collectAsState()

    LaunchedEffect(userId) {
        personalMovieViewModel.getCommentsFromPersonalList(userId, selectedMovieId)
        personalMovieViewModel.observeCommentsForMovieFromPersonalList(userId, selectedMovieId)
    }

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
                            text = SimpleDateFormat(
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
