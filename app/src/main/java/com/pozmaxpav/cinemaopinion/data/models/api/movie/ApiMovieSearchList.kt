package com.pozmaxpav.cinemaopinion.data.models.api.movie

data class ApiMovieSearchList(
    val totalPages: Int,
    val items: List<ApiMovieSearch>
)

data class ApiMovieSearchList2(
    val pagesCount: Int,
    val films: List<ApiMovieSearch2>
)