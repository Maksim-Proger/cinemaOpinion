package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class ObserveListCommentsUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        repository.observeListComments(listId, movieId, onCommentsUpdated)
    }

    fun removeListener() {
        repository.removeCommentsListener()
    }
}