package com.pozmaxpav.cinemaopinion.data.models.api.movie

data class ApiMovieTopList(
    val pagesCount: Int,
    val films: List<ApiMovieTop>
)
