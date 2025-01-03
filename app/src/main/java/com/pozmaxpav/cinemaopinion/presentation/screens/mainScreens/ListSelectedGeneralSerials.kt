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
import androidx.compose.material.icons.Icons
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
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.presentation.components.CustomLottieAnimation
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.ShowSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
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
fun ListSelectedGeneralSerials(
    navController: NavHostController,
    viewModel: FirebaseViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    viewModelMain: MainViewModel = hiltViewModel()
) {

    val listSerials by viewModel.movies.collectAsState()
    val listComments by viewModel.comments.collectAsState()
    var selectedSerial by remember { mutableStateOf<SelectedMovie?>(null) }
    val user by userViewModel.users.collectAsState()
    var username by remember { mutableStateOf("") }
    val info by viewModelMain.informationMovie.collectAsState()
    var (comment, setComment) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var openBottomSheetComments by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getMovies(NODE_LIST_SERIALS)
        viewModel.observeListMovies(NODE_LIST_SERIALS)
        userViewModel.fitchUser()
    }
    LaunchedEffect(selectedSerial) {
        if (selectedSerial != null) { // TODO: Надо проверить на утечку запросов
            viewModelMain.getInformationMovie(selectedSerial!!.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 45.dp, horizontal = 16.dp)
    ) {
        if (user != null) {
            user.let { userInfo ->
                username = userInfo?.firstName ?: "Таинственный пользователь"
            }
        } else {
            username = "Таинственный пользователь"
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
                        title = "Описание",
                        description = info?.description ?: "К сожалению, суточный лимит закончился"
                    )
                },
                commentButton = {
                    Button(
                        onClick = { openBottomSheetComments = !openBottomSheetComments },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Оставить комментарий")
                    }
                },
                movieTransferButton = {
                    Button(
                        onClick = {
                            viewModel.sendingToTheViewedFolder(selectedSerial!!.id.toDouble())
                            showToast(context, "Сериал успешно перенесен")
                            viewModel.savingChangeRecord(
                                username,
                                "переместил(а) фильм: ${selectedSerial!!.nameFilm}"
                            )
                        }
                    ) {
                        Text(text = "Просмотрен")
                    }
                },
                onClick = { selectedSerial = null }
            )
            BackHandler {
                selectedSerial = null
            }

        } else {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(listSerials) { movie ->

                        var isVisible by remember { mutableStateOf(true) } // Состояние видимости

                        AnimatedVisibility(
                            visible = isVisible,
                            exit = slideOutHorizontally(
                                targetOffsetX = { -it }, // Уходит влево
                                animationSpec = tween(durationMillis = 300) // Длительность анимации
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
                                                isVisible =
                                                    false // Скрываем элемент перед удалением
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    delay(300)
                                                    viewModel.removeMovie(movie.id.toDouble())
                                                }
                                                viewModel.savingChangeRecord(
                                                    username,
                                                    "удалил(а) фильм: ${movie.nameFilm}"
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navigateFunction(navController, Route.MainScreen.route)
                        }
                    ),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.list_selected_movies_button_close),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Просмотренные",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        // endregion
    }
}

@Composable
fun ShowCommentGeneralListSerials(
    listComments: List<DomainComment>,
    id: Double,
    firebaseViewModel: FirebaseViewModel = hiltViewModel(),
) {
    val stateComments by firebaseViewModel.commentsDownloadStatus.collectAsState()

    LaunchedEffect(id) {
        firebaseViewModel.getComments(NODE_LIST_SERIALS, id)
        firebaseViewModel.observeComments(NODE_LIST_SERIALS, id)
    }

    when(stateComments) {
        is State.Loading -> {
            CustomLottieAnimation(
                nameFile = "loading_animation.lottie",
                modifier = Modifier.scale(0.5f)
            )
        }
        is State.Success -> {
            LazyColumn(
                contentPadding = PaddingValues(16.dp)
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
                            Text(
                                text = comment.username,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = comment.commentText,
                                style = MaterialTheme.typography.bodyLarge
                            )
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
}