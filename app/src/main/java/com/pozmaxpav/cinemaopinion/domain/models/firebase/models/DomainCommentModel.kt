package com.pozmaxpav.cinemaopinion.domain.models.firebase.models

data class DomainCommentModel(
    val commentId: String = "",
    val username: String = "",
    val commentText: String = "",
    val timestamp: Long = 0
)
