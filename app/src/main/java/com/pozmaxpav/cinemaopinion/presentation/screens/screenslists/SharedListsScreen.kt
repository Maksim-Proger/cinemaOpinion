package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import com.pozmaxpav.cinemaopinion.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SharedListItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.NotificationViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel

@Composable
fun SharedListsScreen(
    userId: String,
    userName: String,
    navController: NavHostController,
    addButton: Boolean = false,
    selectedMovie: DomainSelectedMovieModel? = null,
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    onCloseSharedLists: () -> Unit = {}
) {
    val context = LocalContext.current

    val listState = rememberLazyListState()
    val lists by sharedListsViewModel.list.collectAsState()

    LaunchedEffect(userId) {
        sharedListsViewModel.getLists(userId)
        sharedListsViewModel.observeLists(userId)
    }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(lists, key = { it.listId }) { item ->
                var isVisible by remember { mutableStateOf(true) }
                LaunchedEffect(isVisible) {
                    if (!isVisible) {
                        sharedListsViewModel.removeList(item.listId)
//                        notificationViewModel.createNotification(
//                            context = context,
//                            username = userName,
//                            sharedListId = item.listId,
//                            title = item.title,
//                            stringResourceId = R.string.record_deleted_the_list
//                        )
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
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SharedListItem(
                                item = item,
                                onClick = {
                                    if (addButton) {
                                        selectedMovie?.let { movie ->
                                            sharedListsViewModel.addMovie(
                                                listId = item.listId,
                                                selectedMovie = movie
                                            )
                                            notificationViewModel.createNotification(
                                                context = context,
                                                entityId = movie.id,
                                                sharedListId = item.listId,
                                                username = userName,
                                                stringResourceId = R.string.record_added_movie,
                                                title = movie.nameFilm
                                            )
                                        }
                                        onCloseSharedLists()
                                    } else {
                                        navController.navigate(
                                            Route.ListSharedScreen.getListId(
                                                userName = userName,
                                                listId = item.listId,
                                                title = item.title
                                            )
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

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
}
