package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.DetailsCardSelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSharedScreen(
    navController: NavHostController,
    listId: String,
    title: String,
    sharedListsViewModel: SharedListsViewModel = hiltViewModel()
) {

    val movies by sharedListsViewModel.movies.collectAsState()
    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(listId) { sharedListsViewModel.getMoviesFroSpecialList(listId) }

    Scaffold(
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleString = title,
                scrollBehavior = scrollBehavior,
                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            selectedMovie?.let { movie ->
                DetailsCardSelectedMovie(
                    movie = movie,
                    onClick = { selectedMovie = null }
                )
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