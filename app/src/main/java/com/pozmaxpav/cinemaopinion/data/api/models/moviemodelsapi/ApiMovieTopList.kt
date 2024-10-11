package com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi

data class ApiMovieTopList(
    val pagesCount: Int,
    val films: List<ApiMovieTop>
)
