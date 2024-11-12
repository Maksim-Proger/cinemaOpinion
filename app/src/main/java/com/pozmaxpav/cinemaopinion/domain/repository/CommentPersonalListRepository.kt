package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.CommentPersonalListModel
import kotlinx.coroutines.flow.Flow

interface CommentPersonalListRepository {
    suspend fun insert(comment: CommentPersonalListModel)
    suspend fun getComments(): Flow<List<CommentPersonalListModel>>
}