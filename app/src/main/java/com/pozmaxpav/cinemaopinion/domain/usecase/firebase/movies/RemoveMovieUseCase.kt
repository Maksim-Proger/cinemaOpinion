package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class RemoveMovieUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, id: Double) {
        firebaseRepository.removeMovie(dataSource, id)
    }
}