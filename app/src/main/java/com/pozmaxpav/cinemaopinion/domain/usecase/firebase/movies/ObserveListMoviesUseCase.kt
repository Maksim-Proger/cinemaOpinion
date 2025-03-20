package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, onMoviesUpdated: (List<SelectedMovieModel>) -> Unit) {
        firebaseRepository.observeListMovies(dataSource, onMoviesUpdated)
    }
}