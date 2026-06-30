package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.ui.presentation.components.topappbar.SpecialTopAppBar
import com.example.ui.presentation.theme.cardAccent
import com.example.ui.presentation.theme.onCardAccent
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.presentation.components.detailscards.NewDesignMovieDetailScreen
import com.pozmaxpav.cinemaopinion.presentation.components.items.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.AdaptiveBackHandler
import com.pozmaxpav.cinemaopinion.presentation.viewModels.api.ApiViewModel
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiListScreen(
    navController: NavHostController,
    listType: String,
    userId: String
) {

    val listState = rememberLazyListState()
    val pagedListState = rememberLazyListState()

    val isAtTop by remember {
        derivedStateOf {
            // выбираем нужный state в зависимости от типа списка
            val activeState = if (listType == "premiere") listState else pagedListState
            activeState.firstVisibleItemIndex == 0 && activeState.firstVisibleItemScrollOffset == 0
        }
    }

    val parentEntry = remember(navController) {
        navController.previousBackStackEntry!!
    }
    val apiViewModel = hiltViewModel<ApiViewModel>(parentEntry)

    var selectedMovie by remember { mutableStateOf<MovieData?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            if (selectedMovie != null) {
                NewDesignMovieDetailScreen(
                    movie = selectedMovie,
                    userId = userId,
                    onCloseButton = { selectedMovie = null },
                    navController = navController
                )

                AdaptiveBackHandler { selectedMovie = null }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    when (listType) {
                        "premiere" -> {
                            val premiereMovies by apiViewModel.premiersMovies.collectAsStateWithLifecycle()
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(WindowInsets.statusBars.asPaddingValues())
                                    .padding(top = if (isAtTop) TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp)
                                    .padding(horizontal = 5.dp),
                            ) {
                                items(premiereMovies, key = { it.id }) { movie ->
                                    Card(
                                        modifier = Modifier.wrapContentHeight(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.cardAccent,
                                            contentColor = MaterialTheme.colorScheme.onCardAccent
                                        )
                                    ) {
                                        SelectedMovieItem(
                                            movieData = movie,
                                            selectedMovie = null,
                                            onClick = { selectedMovie = movie }
                                        )
                                    }
                                    Spacer(Modifier.padding(5.dp))
                                }
                            }
                        }

                        else -> {
                            val pagingFlow = remember(listType) {
                                when (listType) {
                                    "top" -> apiViewModel.topMoviesPaging
                                    "search" -> apiViewModel.searchMoviesPaging
                                    else -> emptyFlow<PagingData<MovieData>>()
                                }
                            }
                            PagedMovieList(
                                movies = pagingFlow.collectAsLazyPagingItems(),
                                onMovieClick = { selectedMovie = it },
                                listState = pagedListState,
                                isAtTop = isAtTop
                            )
                        }
                    }
                }
            }
        }
        if (selectedMovie == null) {
            SpecialTopAppBar(
                isAtTop = isAtTop,
                title = "Тестовая страница",
                goToBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PagedMovieList(
    movies: LazyPagingItems<MovieData>,
    onMovieClick: (MovieData) -> Unit,
    listState: LazyListState,
    isAtTop: Boolean
) {

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (movies.loadState.refresh) {
                    is LoadState.Loading -> CenteredBox {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    }

                    is LoadState.Error -> CenteredBox {
                        Text(
                            text = "При загрузке произошла ошибка.",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(WindowInsets.statusBars.asPaddingValues())
                                .padding(top = if (isAtTop) TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp)
                                .padding(horizontal = 5.dp),
                        ) {
                            items(
                                count = movies.itemCount,
                                key = movies.itemKey { it.id }
                            ) { index ->
                                Card(
                                    modifier = Modifier.wrapContentHeight(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.cardAccent,
                                        contentColor = MaterialTheme.colorScheme.onCardAccent
                                    )
                                ) {
                                    movies[index]?.let { movie ->
                                        SelectedMovieItem(
                                            movieData = movie,
                                            selectedMovie = null,
                                            onClick = { onMovieClick(movie) }
                                        )
                                    }
                                }
                                Spacer(Modifier.padding(5.dp))
                            }
                            if (movies.loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteredBox(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
