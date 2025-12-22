package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(
    private val repo: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int): DomainSelectedMovieModel? {
        return repo.getMovieById(listId, movieId)
    }
}