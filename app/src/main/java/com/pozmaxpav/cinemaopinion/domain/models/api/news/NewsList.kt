package com.pozmaxpav.cinemaopinion.domain.models.api.news

class NewsList(
    val total: Int,
    val totalPages: Int,
    val items: List<NewsModel>
)
