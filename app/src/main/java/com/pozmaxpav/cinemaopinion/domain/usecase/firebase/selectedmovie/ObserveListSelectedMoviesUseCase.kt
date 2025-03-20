package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class ObserveListSelectedMoviesUseCase @Inject constructor(
    private val selectedMovieRepository: SelectedMovieRepository
) {
    suspend operator fun invoke(userId: String, onSelectedMoviesUpdated: (List<SelectedMovieModel>) -> Unit) {
        selectedMovieRepository.observeListSelectedMovies(userId, onSelectedMoviesUpdated)
    }

    fun removeListener() {
        selectedMovieRepository.removeListener()
    }
}