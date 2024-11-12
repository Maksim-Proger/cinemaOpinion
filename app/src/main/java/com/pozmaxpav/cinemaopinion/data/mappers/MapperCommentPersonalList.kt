package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.localdb.entities.CommentPersonalListEntity
import com.pozmaxpav.cinemaopinion.domain.models.CommentPersonalListModel

fun CommentPersonalListModel.toEntity(): CommentPersonalListEntity {
    return CommentPersonalListEntity(
        id = id,
        movieId = movieId,
        commentText = commentText,
        timestamp = timestamp
    )
}

fun CommentPersonalListEntity.toDomain(): CommentPersonalListModel {
    return CommentPersonalListModel(
        id = id,
        movieId = movieId,
        commentText = commentText,
        timestamp = timestamp
    )
}