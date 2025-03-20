package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(dataSource: String, selectedMovie: SelectedMovieModel) {
        firebaseRepository.saveMovie(dataSource, selectedMovie)
    }
}