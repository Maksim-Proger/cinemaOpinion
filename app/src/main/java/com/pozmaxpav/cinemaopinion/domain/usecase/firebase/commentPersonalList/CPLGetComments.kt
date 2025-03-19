package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.commentPersonalList

import com.pozmaxpav.cinemaopinion.domain.models.room.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.CommentPersonalListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CPLGetComments @Inject constructor(
    private val repository: CommentPersonalListRepository
) {
    suspend operator fun invoke(): Flow<List<CommentPersonalListModel>> {
        return repository.getComments()
    }
}