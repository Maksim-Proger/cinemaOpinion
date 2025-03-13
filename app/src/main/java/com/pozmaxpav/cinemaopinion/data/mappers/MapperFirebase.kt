package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.remote.firebase.models.DataChangelogModel
import com.pozmaxpav.cinemaopinion.data.remote.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment

fun DataComment.commentToDomain(): DomainComment {
    return DomainComment(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}

fun DomainComment.commentToData(): DataComment {
    return DataComment(
        commentId = commentId,
        username = username,
        commentText = commentText,
        timestamp = timestamp
    )
}

fun DataChangelogModel.toDomainChangelog(): DomainChangelogModel {
    return DomainChangelogModel(
        noteId = noteId,
        username = username,
        noteText = noteText,
        timestamp = timestamp
    )
}

fun DomainChangelogModel.toDataChangelog(): DataChangelogModel {
    return DataChangelogModel(
        noteId = noteId,
        username = username,
        noteText = noteText,
        timestamp = timestamp
    )
}

