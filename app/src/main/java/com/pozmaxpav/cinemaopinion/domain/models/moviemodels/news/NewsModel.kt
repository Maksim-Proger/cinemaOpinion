package com.pozmaxpav.cinemaopinion.domain.models.moviemodels.news

data class NewsModel(
    val kinopoiskId: Int,
    val imageUrl: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val publishedAt: String?,
)
