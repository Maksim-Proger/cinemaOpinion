package com.pozmaxpav.cinemaopinion.data.firebase.mappers

import com.pozmaxpav.cinemaopinion.data.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment

fun DataComment.toDomain(): DomainComment {
    return DomainComment(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}

fun DomainComment.toData(): DataComment {
    return DataComment(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}