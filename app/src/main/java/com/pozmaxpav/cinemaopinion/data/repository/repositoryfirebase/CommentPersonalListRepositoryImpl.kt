package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.data.local.room.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.mappers.toEntity
import com.pozmaxpav.cinemaopinion.domain.models.room.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.repository.remote.CommentPersonalListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentPersonalListRepositoryImpl @Inject constructor(
    private val commentPersonalListDao: CommentPersonalListDao
) : CommentPersonalListRepository {
    override suspend fun insert(comment: CommentPersonalListModel) {
        commentPersonalListDao.insert(comment.toEntity())
    }
    override suspend fun getComments(): Flow<List<CommentPersonalListModel>> {
        return commentPersonalListDao.getComments().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
