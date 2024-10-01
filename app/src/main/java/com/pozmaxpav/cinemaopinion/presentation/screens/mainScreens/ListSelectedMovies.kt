package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie


@Composable
fun ListSelectedMovies(
    viewModel: SelectedMovieViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {

    val listSelectedMovies by viewModel.selectedMovies.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fitchListSelectedMovies()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 45.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(listSelectedMovies) { movie ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            WorkerWithImageSelectedMovie(
                                movie = movie,
                                height = 90.dp
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                            Text(
                                text = movie.nameFilm,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = movie.nameFilm,
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(15.dp))

        Button(
            onClick = { onDismiss() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = "Закрыть",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

//    Scaffold(
//        modifier = Modifier
//            .fillMaxSize()
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Transparent)
//                .padding(vertical = 45.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(MaterialTheme.colorScheme.tertiaryContainer)
//            ) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentPadding = PaddingValues(16.dp)
//                ) {
//                    items(listSelectedMovies) { movie ->
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(55.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = MaterialTheme.colorScheme.secondary,
//                                contentColor = MaterialTheme.colorScheme.onSecondary
//                            )
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = movie.nameFilm,
//                                    style = MaterialTheme.typography.titleMedium
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.padding(15.dp))
//
//            Button(
//                onClick = { onDismiss() },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            ) {
//                Text(
//                    text = "Закрыть",
//                    style = MaterialTheme.typography.bodyLarge
//                )
//            }
//        }
//    }
}

