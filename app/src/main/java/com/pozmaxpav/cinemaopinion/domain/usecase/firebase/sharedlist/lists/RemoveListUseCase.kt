package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.lists

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class RemoveListUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String) {
        repository.removeList(listId)
    }
}