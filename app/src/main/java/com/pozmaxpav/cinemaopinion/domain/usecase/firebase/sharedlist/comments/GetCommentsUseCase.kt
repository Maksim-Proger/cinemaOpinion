package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.comments

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(listId: String, movieId: Int) : List<DomainCommentModel> {
        return repository.getComments(listId, movieId)
    }
}