package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import javax.inject.Inject

class ObserveListSelectedMoviesUseCase @Inject constructor(
    private val repository: PersonalMovieRepository
) {
    suspend operator fun invoke(userId: String, onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        repository.observeListSelectedMovies(userId, onSelectedMoviesUpdated)
    }

    fun removeListener() {
        repository.removeSelectedMoviesListener()
    }
}