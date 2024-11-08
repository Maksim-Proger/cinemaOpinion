package com.pozmaxpav.cinemaopinion.data.firebase.mappers

import com.pozmaxpav.cinemaopinion.data.firebase.models.DataChangelogModel
import com.pozmaxpav.cinemaopinion.data.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
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