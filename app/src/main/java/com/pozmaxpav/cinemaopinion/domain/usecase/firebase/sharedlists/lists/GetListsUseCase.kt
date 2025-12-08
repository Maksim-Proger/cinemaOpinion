package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.lists

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class GetListsUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(userId: String) : List<DomainSharedListModel> {
        return repository.getLists(userId)
    }
}