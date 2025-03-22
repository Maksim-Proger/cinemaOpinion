package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val firebaseRepository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        firebaseRepository.observeListMovies(dataSource, onMoviesUpdated)
    }
}