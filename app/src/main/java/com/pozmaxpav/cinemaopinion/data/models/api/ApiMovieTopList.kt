package com.pozmaxpav.cinemaopinion.data.models.api

data class ApiMovieTopList(
    val pagesCount: Int,
    val films: List<ApiMovieTop>
)
