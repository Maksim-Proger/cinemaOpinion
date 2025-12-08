package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int, comment: DomainCommentModel) {
        repository.addComment(listId, movieId, comment)
    }
}