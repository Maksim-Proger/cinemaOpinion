package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class MoveMovieUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(
        listId: String,
        sourceNode: String,
        destination: String,
        movieId: Int
    ) {
        repository.moveMovie(listId, sourceNode, destination, movieId)
    }
}
