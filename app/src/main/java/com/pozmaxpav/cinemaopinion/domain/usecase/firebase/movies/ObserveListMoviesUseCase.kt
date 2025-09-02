package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(dataSource: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        repository.observeListMovies(dataSource, onMoviesUpdated)
    }

    fun removeListener() {
        repository.removeMoviesListener()
    }
}