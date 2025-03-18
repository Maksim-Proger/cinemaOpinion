package com.pozmaxpav.cinemaopinion.domain.repository.remote

import com.pozmaxpav.cinemaopinion.domain.models.room.models.CommentPersonalListModel
import kotlinx.coroutines.flow.Flow

interface CommentPersonalListRepository {
    suspend fun insert(comment: CommentPersonalListModel)
    suspend fun getComments(): Flow<List<CommentPersonalListModel>>
}