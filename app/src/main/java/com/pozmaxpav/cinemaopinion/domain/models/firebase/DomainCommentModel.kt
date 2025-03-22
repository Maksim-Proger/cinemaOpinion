package com.pozmaxpav.cinemaopinion.domain.models.firebase

data class DomainCommentModel(
    val commentId: String = "",
    val username: String = "",
    val commentText: String = "",
    val timestamp: Long = 0
)
