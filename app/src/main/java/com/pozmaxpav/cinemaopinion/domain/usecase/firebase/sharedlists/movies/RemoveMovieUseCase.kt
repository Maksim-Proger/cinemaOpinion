package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class RemoveMovieUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int) {
        repository.removeMovie(listId, movieId)
    }
}