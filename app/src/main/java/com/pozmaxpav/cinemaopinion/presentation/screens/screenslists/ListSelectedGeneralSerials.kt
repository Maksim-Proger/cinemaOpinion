package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import android.os.Build
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
import androidx.compose.foundation.layout.size
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
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WAITING_CONTINUATION_SERIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.ShowCommentList
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@Composable
fun ListSelectedGeneralSerials(
    navController: NavHostController,
    fireBaseMovieViewModel: FireBaseMovieViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel(),
) {

    val listSerials by fireBaseMovieViewModel.movies.collectAsState()
    var selectedSerial by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
    val userId by mainViewModel.userId.collectAsState()
    val userData by auxiliaryUserViewModel.userData.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    val (comment, setComment) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        fireBaseMovieViewModel.getMovies(NODE_LIST_SERIALS)
        fireBaseMovieViewModel.observeListMovies(NODE_LIST_SERIALS)
    }
    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
    }
    LaunchedEffect(selectedSerial) {
        selectedSerial?.let { movie ->
            apiViewModel.getInformationMovie(movie.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 50.dp)
    ) {

        if (openBottomSheetChange) {
            MyBottomSheet(
                onClose = { openBottomSheetChange = false },
                content = {
                    userData?.let { user ->
                        selectedSerial?.let { serial ->
                            selectedComment?.let { comment ->
                                ChangeComment(
                                    dataSource = NODE_LIST_SERIALS,
                                    userName = user.nikName,
                                    selectedMovieId = serial.id,
                                    selectedComment = comment,
                                    viewModel = fireBaseMovieViewModel
                                ) {
                                    openBottomSheetChange = false
                                }
                            }
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
                                        NODE_LIST_SERIALS,
                                        selectedSerial!!.id.toDouble(),
                                        userData!!.nikName,
                                        comment
                                    )
                                    fireBaseMovieViewModel.savingChangeRecord(
                                        context,
                                        userData!!.nikName,
                                        R.string.record_added_comment_to_series,
                                        selectedSerial!!.nameFilm,
                                        NODE_LIST_SERIALS,
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

        if (selectedSerial == null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigateFunction(navController, Route.MainScreen.route) }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.description_icon_back_button),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    text = "Список с сериалами",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        if (selectedSerial != null) {
            DetailsCardSelectedMovie(
                movie = selectedSerial!!,
                content = {
                    ShowCommentList(
                        dataSource = NODE_LIST_SERIALS,
                        selectedMovieId = selectedSerial!!.id,
                        viewModel = fireBaseMovieViewModel,
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
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = { openBottomSheetComments = !openBottomSheetComments }
                    )
                },
                movieTransferButtonToWaitingList = {
                    CustomTextButton(
                        textButton = context.getString(R.string.button_move_to_waiting_list),
                        topPadding = 7.dp,
                        bottomPadding = 7.dp,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClickButton = {
                            fireBaseMovieViewModel.sendingToNewDirectory(
                                NODE_LIST_SERIALS,
                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                selectedSerial!!.id.toDouble()
                            )
                            showToast(context, R.string.series_has_been_moved_to_waiting_list)
                            fireBaseMovieViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_series_has_been_moved_to_waiting_list,
                                selectedSerial!!.nameFilm,
                                NODE_LIST_WAITING_CONTINUATION_SERIES,
                                selectedSerial!!.id
                            )
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
                            fireBaseMovieViewModel.sendingToNewDirectory(
                                NODE_LIST_SERIALS,
                                NODE_LIST_WATCHED_MOVIES,
                                selectedSerial!!.id.toDouble()
                            )
                            showToast(context, R.string.series_has_been_moved)
                            fireBaseMovieViewModel.savingChangeRecord(
                                context,
                                userData!!.nikName,
                                R.string.record_series_has_been_moved_to_viewed,
                                selectedSerial!!.nameFilm,
                                NODE_LIST_WATCHED_MOVIES,
                                selectedSerial!!.id
                            )
                        }
                    )
                },
                onClick = {
                    selectedSerial = null
                }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                OnBackInvokedHandler { selectedSerial = null }
            } else {
                BackHandler { selectedSerial = null }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(listSerials, key = { it.id }) { movie ->

                        var isVisible by remember { mutableStateOf(true) }

                        LaunchedEffect(isVisible) {
                            if (!isVisible) {
                                fireBaseMovieViewModel.removeMovie(
                                    NODE_LIST_SERIALS,
                                    movie.id
                                )
                                fireBaseMovieViewModel.savingChangeRecord(
                                    context,
                                    userData!!.nikName,
                                    R.string.record_deleted_the_series,
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
                                            onClick = { selectedSerial = movie }
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
