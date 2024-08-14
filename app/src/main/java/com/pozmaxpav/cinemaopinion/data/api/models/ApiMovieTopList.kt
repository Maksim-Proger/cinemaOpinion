package com.pozmaxpav.cinemaopinion.data.api.models

data class ApiMovieTopList(
    val pagesCount: Int,
    val films: List<ApiMovieTop>
)
