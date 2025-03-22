package com.pozmaxpav.cinemaopinion.data.models.api.news

data class ApiNewsModel(
    val kinopoiskId: Int,
    val imageUrl: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val publishedAt: String?,
)
