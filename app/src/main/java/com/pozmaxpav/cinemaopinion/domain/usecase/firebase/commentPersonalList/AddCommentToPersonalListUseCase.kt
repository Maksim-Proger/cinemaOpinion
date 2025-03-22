package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.commentPersonalList

import com.pozmaxpav.cinemaopinion.domain.models.room.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.CommentPersonalListRepository
import javax.inject.Inject

class AddCommentToPersonalListUseCase @Inject constructor(
    private val repository: CommentPersonalListRepository
) {
    suspend operator fun invoke(comment: CommentPersonalListModel) {
        repository.insert(comment)
    }
}