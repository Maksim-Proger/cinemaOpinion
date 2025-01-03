package com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi

data class ApiMovieSearchList(
    val totalPages: Int,
    val items: List<ApiMovieSearch>
)

data class ApiMovieSearchList2(
    val pagesCount: Int,
    val films: List<ApiMovieSearch2>
)