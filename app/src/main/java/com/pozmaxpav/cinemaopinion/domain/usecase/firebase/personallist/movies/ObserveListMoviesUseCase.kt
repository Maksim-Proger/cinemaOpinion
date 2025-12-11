package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        repository.observeListMovies(userId, onSelectedMoviesUpdated)
    }

    fun removeListener() {
        repository.removeMoviesListener()
    }
}