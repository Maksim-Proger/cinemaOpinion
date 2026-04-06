package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class ObserveListMoviesUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, destination: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        repository.observeListMovies(listId,destination, onMoviesUpdated)
    }

    fun removeListener() {
        repository.removeMoviesListener()
    }
}