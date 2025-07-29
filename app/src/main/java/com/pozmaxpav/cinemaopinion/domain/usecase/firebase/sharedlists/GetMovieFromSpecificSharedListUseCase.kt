package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class GetMovieFromSpecificSharedListUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String) : List<DomainSelectedMovieModel> {
        return repository.getMovieFromSpecificSharedList(listId)
    }
}