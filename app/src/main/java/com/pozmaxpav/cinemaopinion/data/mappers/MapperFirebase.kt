package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.models.firebase.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel

fun DataComment.commentToDomain(): DomainCommentModel {
    return DomainCommentModel(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}

fun DomainCommentModel.commentToData(): DataComment {
    return DataComment(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}


