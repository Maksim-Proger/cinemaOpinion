package com.pozmaxpav.cinemaopinion.domain.usecase.commentPersonalList

import com.pozmaxpav.cinemaopinion.domain.models.room.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.repository.CommentPersonalListRepository
import javax.inject.Inject

class CPLInsertUseCase @Inject constructor(
    private val repository: CommentPersonalListRepository
) {
    suspend operator fun invoke(comment: CommentPersonalListModel) {
        repository.insert(comment)
    }
}