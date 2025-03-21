package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
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
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.ShowSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WAITING_CONTINUATION_SERIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
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
fun ListSelectedGeneralSerials(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val listSerials by firebaseViewModel.movies.collectAsState()
    val listComments by firebaseViewModel.comments.collectAsState()
    var selectedSerial by remember { mutableStateOf<SelectedMovieModel?>(null) }
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()
    val info by mainViewModel.informationMovie.collectAsState()
    val (comment, setComment) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var openBottomSheetComments by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        firebaseViewModel.getMovies(NODE_LIST_SERIALS)
        firebaseViewModel.observeListMovies(NODE_LIST_SERIALS)
    }
    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedSerial) {
        selectedSerial?.let { movie ->
            mainViewModel.getInformationMovie(movie.id)
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
                                if (userData != null) {
                                    firebaseViewModel.addComment(
                                        NODE_LIST_SERIALS,
                                        selectedSerial!!.id.toDouble(),
                                        userData!!.nikName,
                                        comment
                                    )
                                    firebaseViewModel.savingChangeRecord(
                                        context,
                                        userData!!.nikName,
                                        R.string.record_added_comment_to_series,
                                        selectedSerial!!.nameFilm
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

        if (selectedSerial == null) {
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

        if (selectedSerial != null) {
            ShowSelectedMovie(
                movie = selectedSerial!!,
                isGeneralList = true,
                isShowCommentButton = true,
                content = {
                    ShowCommentGeneralListSerials(
                        listComments,
                        selectedSerial!!.id.toDouble()
                    )
                },
                openDescription = {
                    ExpandedCard(
                        title = stringResource(R.string.text_for_expandedCard_field),
                        description = info?.description ?: stringResource(R.string.limit_is_over)
                    )
                },
                commentButton = {
                    Button(
                        onClick = { openBottomSheetComments = !openBottomSheetComments }
                    ) {
                        Text(
                            text = stringResource(R.string.button_leave_comment),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                movieTransferButtonToWaitingList = {
                    Button(
                        onClick = {
                            firebaseViewModel.sendingToTheViewedFolder(
                                NODE_LIST_SERIALS,
                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                selectedSerial!!.id.toDouble()
                            )
                            showToast(context, R.string.series_has_been_moved_to_waiting_list)
                            firebaseViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_series_has_been_moved_to_waiting_list,
                                selectedSerial!!.nameFilm
                            )
                        }
                    ) {
                        Text(
                            text = "Перенести в лист ожидания",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                movieTransferButton = {
                    Button(
                        onClick = {
                            firebaseViewModel.sendingToTheViewedFolder(
                                NODE_LIST_SERIALS,
                                NODE_LIST_WATCHED_MOVIES,
                                selectedSerial!!.id.toDouble()
                            )
                            showToast(context, R.string.series_has_been_moved)
                            firebaseViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_series_has_been_moved_to_viewed,
                                selectedSerial!!.nameFilm
                            )
                        }
                    ) {
                        Text(
                            text = "Просмотрен",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = { selectedSerial = null }
            )
            BackHandler {
                selectedSerial = null
            }

        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(listSerials, key = { it.id }) { movie ->

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
                                    modifier = Modifier
                                        .wrapContentHeight(),
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
                                                onClick = { selectedSerial = movie }
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                isVisible = false
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    delay(300)
                                                    firebaseViewModel.removeMovie(
                                                        NODE_LIST_SERIALS,
                                                        movie.id.toDouble()
                                                    )
                                                }
                                                firebaseViewModel.savingChangeRecord(
                                                    context,
                                                    userData!!.nikName,
                                                    R.string.record_deleted_the_series,
                                                    movie.nameFilm
                                                )
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
        }

        Spacer(modifier = Modifier.padding(15.dp))

        // region Кнопки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .clickable {
                        navigateFunction(navController, Route.ListWatchedMovies.route)
                    },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Просмотренные",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(
                modifier = Modifier
                    .clickable {
                        navigateFunction(navController, Route.ListWaitingContinuationSeries.route)
                    },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = "Лист ожидания",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        // endregion
    }
}

@Composable
fun ShowCommentGeneralListSerials(
    listComments: List<DomainCommentModel>,
    id: Double,
    firebaseViewModel: FirebaseViewModel = hiltViewModel(),
) {
    val stateComments by firebaseViewModel.commentsDownloadStatus.collectAsState()

    LaunchedEffect(id) {
        firebaseViewModel.getComments(NODE_LIST_SERIALS, id)
        firebaseViewModel.observeComments(NODE_LIST_SERIALS, id)
    }

    when (stateComments) {
        is State.Loading -> {
            CustomLottieAnimation(
                nameFile = "loading_animation.lottie",
                modifier = Modifier.scale(0.5f)
            )
        }

        is State.Success -> {
            LazyColumn(
                contentPadding = PaddingValues(5.dp)
            ) {
                items(listComments) { comment ->
                    Card(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
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

        is State.Error -> {}
    }
}
