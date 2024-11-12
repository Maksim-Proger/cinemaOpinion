package com.pozmaxpav.cinemaopinion.domain.models

data class CommentPersonalListModel(
    val id: Int,
    val movieId: Double,
    val commentText: String,
    val timestamp: Long
)
