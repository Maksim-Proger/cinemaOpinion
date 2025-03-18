package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, onMoviesUpdated: (List<SelectedMovie>) -> Unit) {
        firebaseRepository.observeListMovies(dataSource, onMoviesUpdated)
    }
}