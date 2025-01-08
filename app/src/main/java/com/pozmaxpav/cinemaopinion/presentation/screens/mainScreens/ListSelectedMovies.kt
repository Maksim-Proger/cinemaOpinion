package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import android.icu.text.SimpleDateFormat
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.ShowSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.CommentPersonalListViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextFieldForComments
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@Composable
fun ListSelectedMovies(
    navController: NavHostController,
    viewModel: SelectedMovieViewModel = hiltViewModel(),
    viewModelComments: CommentPersonalListViewModel = hiltViewModel(),
    viewModelMain: MainViewModel = hiltViewModel()
) {
    val listSelectedMovies by viewModel.selectedMovies.collectAsState()
    val listComments by viewModelComments.comments.collectAsState()
    val info by viewModelMain.informationMovie.collectAsState()
    var selectedNote by remember { mutableStateOf<SelectedMovie?>(null) }
    var openBottomSheetComments by remember { mutableStateOf(false) }
    var (comment, setComment) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.fitchListSelectedMovies()
    }

    LaunchedEffect(selectedNote) {
        if (selectedNote != null) { // TODO: Надо проверить на утечку запросов
            viewModelMain.getInformationMovie(selectedNote!!.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 45.dp, horizontal = 16.dp)
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
                                viewModelComments.insertComment(
                                    selectedNote!!.id.toDouble(),
                                    comment
                                )
                                showToast(context, "Комментарий добавлен")
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

        if (selectedNote != null) {
            ShowSelectedMovie(
                movie = selectedNote!!,
                isGeneralList = false,
                isShowCommentButton = true,
                content = {
                    ShowListComments(
                        listComments,
                        selectedNote!!.id.toDouble()
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
                        Text(
                            text = "Оставить комментарий",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = { selectedNote = null }
            )
            BackHandler {
                selectedNote = null
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
                    items(listSelectedMovies, key = { it.id }) { movie ->

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
                                            modifier = Modifier.weight(0.9f)
                                        ) {
                                            SelectedMovieItem(
                                                movie = movie,
                                                onClick = { selectedNote = movie }
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                isVisible =
                                                    false // Скрываем элемент перед удалением
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    delay(300)
                                                    viewModel.deleteSelectedMovie(movie)
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
        }

        Spacer(modifier = Modifier.padding(15.dp))

        // region Кнопка закрыть
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
        // endregion
    }
}

@Composable
private fun ShowListComments(
    listComments: List<CommentPersonalListModel>,
    id: Double,
    viewModelComments: CommentPersonalListViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModelComments.getCommentsList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (listComments.isNotEmpty()) {
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
                    if (id == comment.movieId) {
                        Column(modifier = Modifier.padding(8.dp)) { // TODO: Переделать стили
                            Text(
                                text = comment.commentText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = SimpleDateFormat(
                                    "dd.MM.yyyy HH:mm",
                                    Locale.getDefault()
                                ).format(
                                    Date(comment.timestamp)
                                )

                            )
                        }
                    }
                }
            }
        }
    }
}
