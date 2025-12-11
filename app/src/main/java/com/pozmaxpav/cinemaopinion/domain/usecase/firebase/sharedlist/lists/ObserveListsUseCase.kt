package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.lists

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class ObserveListsUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(userId: String, onListsUpdated: (List<DomainSharedListModel>) -> Unit) {
        repository.observeLists(userId, onListsUpdated)
    }

    fun removeListener() {
        repository.removeListsListener()
    }
}