package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String): List<SelectedMovieModel> {
        return firebaseRepository.getMovie(dataSource)
    }
}