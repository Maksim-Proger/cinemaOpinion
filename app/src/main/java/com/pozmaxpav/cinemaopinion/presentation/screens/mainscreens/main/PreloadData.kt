package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel

@Composable
fun PreloadData(
    userId: String,
    personalMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    sharedListsViewModel: SharedListsViewModel = hiltViewModel(),
    seriesControlViewModel: SeriesControlViewModel = hiltViewModel(),
    resData: @Composable (
        List<DomainSelectedMovieModel>,
        List<DomainSharedListModel>,
        List<DomainSeriesControlModel>
    ) -> Unit
) {
    val countPersonalMovies by personalMovieViewModel.listSelectedMovies.collectAsState()
    val countSharedLists by sharedListsViewModel.list.collectAsState()
    val countSeriesControlItems by seriesControlViewModel.listMovies.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotBlank() && userId != "Unknown") {
            sharedListsViewModel.getLists(userId)
            seriesControlViewModel.getListEntries(userId)
            personalMovieViewModel.getMovies(userId)
        }
    }

    resData(countPersonalMovies, countSharedLists, countSeriesControlItems)
}
