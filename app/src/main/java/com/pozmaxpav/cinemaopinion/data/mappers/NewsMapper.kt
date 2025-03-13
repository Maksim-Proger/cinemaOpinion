package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.news.ApiNewsList
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.news.ApiNewsModel
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsModel

fun ApiNewsModel.toDomain(): NewsModel {
    return NewsModel(
        kinopoiskId = kinopoiskId,
        imageUrl = imageUrl,
        title = title,
        description = description,
        url = url,
        publishedAt = publishedAt
    )
}

fun ApiNewsList.toDomain(): NewsList {
    return NewsList(
        total = total,
        totalPages = totalPages,
        items = items.map { it.toDomain() }
    )
}

