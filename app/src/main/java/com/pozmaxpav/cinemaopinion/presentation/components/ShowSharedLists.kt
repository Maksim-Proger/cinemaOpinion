package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SharedListItem
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@Composable
fun ShowSharedLists(
    userId: String,
    navController: NavHostController,
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    addButton: Boolean = false,
    selectedMovie: DomainSelectedMovieModel? = null
) {
    val lists by sharedListsViewModel.list.collectAsState()

    LaunchedEffect(userId) { sharedListsViewModel.getLists(userId) }

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(16.dp)
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(lists) { item ->
                SharedListItem(
                    item = item,
                    onClick = {
                        if (addButton) {
                            selectedMovie?.let { sharedListsViewModel.addMovie(item.listId, it) }
                        } else {
                            navController.navigate(
                                Route.ListSharedScreen.getListId(
                                    listId = item.listId,
                                    title = item.title
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

