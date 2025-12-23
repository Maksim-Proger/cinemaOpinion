package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.system

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class SendMoviesUseCase @Inject constructor(
    private val repo: SharedListsRepository
) {
    suspend operator fun invoke(sourceNode: String, listId: String) {
        repo.sendingAllMoviesToNewDirectory(sourceNode, listId)
    }
}