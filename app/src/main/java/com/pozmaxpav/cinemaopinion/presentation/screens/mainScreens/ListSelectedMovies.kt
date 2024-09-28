package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel


@Composable
fun ListSelectedMovies(
    viewModel: SelectedMovieViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {

    val listSelectedMovies by viewModel.selectedMovies.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fitchListSelectedMovies()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.7f)
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(listSelectedMovies) { movie ->
                    Card(
                       modifier = Modifier
                           .fillMaxWidth()
                           .wrapContentHeight()
                           .padding(innerPadding)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 25.dp),
                            text = movie.nameFilm
                        )
                    }
                }
            }

            Button(onClick = { onDismiss() }) {
                Text("Закрыть")
            }
        }
    }
}

