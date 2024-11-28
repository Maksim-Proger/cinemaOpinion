package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.ProgressBar
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.SelectedMovieItem
import com.pozmaxpav.cinemaopinion.utilits.ShowSelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.state.State
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWatchedMovies(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listMovies by firebaseViewModel.movies.collectAsState()
    val comments by firebaseViewModel.comments.collectAsState()
    var selectedNote by remember { mutableStateOf<SelectedMovie?>(null) }
    var showTopBar by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val state by firebaseViewModel.state.collectAsState() // TODO: Для комментариев нужна новая переменная состояния?

    LaunchedEffect(Unit) {
        firebaseViewModel.getMovies(NODE_LIST_WATCHED_MOVIES)
    }

    LaunchedEffect(selectedNote) { // TODO: Надо наж этим еще подумать!
        firebaseViewModel.getMovies(NODE_LIST_WATCHED_MOVIES)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!showTopBar) {
                ClassicTopAppBar(
                    title = "Просмотренные фильмы",
                    scrollBehavior = scrollBehavior,
                    onTransitionAction = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }
                )
            }
        }
    ) { innerPadding ->

        if (selectedNote != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 45.dp, horizontal = 16.dp)
            ) {
                ShowSelectedMovie(
                    movie = selectedNote!!,
                    buttonVisibility = false,
                    content = {
                        ShowCommentWatchedMoviesList(
                            listComments = comments,
                            id = selectedNote!!.id.toDouble()
                        )
                    },
                    onClick = {
                        selectedNote = null
                        showTopBar = !showTopBar
                    }
                )

                BackHandler {
                    selectedNote = null
                    showTopBar = !showTopBar
                }
            }
        } else {
            when (state) {
                is State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center // Центрируем содержимое
                    ) {
                        ProgressBar()
                    }
                }
                is State.Success -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(listMovies) { movie ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    )
                                ) {
                                    SelectedMovieItem(
                                        movie = movie,
                                        onClick = { selectedNote = movie },
                                        showTopBar = { showTopBar = !showTopBar }
                                    )
                                }
                            }
                            Spacer(Modifier.padding(5.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowCommentWatchedMoviesList(
    listComments: List<DomainComment>,
    id: Double,
    viewModel: FirebaseViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.getComments(NODE_LIST_WATCHED_MOVIES, id)
    }

    LaunchedEffect(id) {
        viewModel.observeComments(NODE_LIST_WATCHED_MOVIES, id)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp)
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
                Column(modifier = Modifier.padding(8.dp)) { // TODO: Переделать стили
                    Text(text = comment.username, fontWeight = FontWeight.Bold)
                    Text(text = comment.commentText)
                    Text(
                        text =
                        SimpleDateFormat(
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