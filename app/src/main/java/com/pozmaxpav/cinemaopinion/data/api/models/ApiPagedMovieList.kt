package com.pozmaxpav.cinemaopinion.data.api.models

data class ApiPagedMovieList(
    val pagesCount: Int,
    val films: List<ApiMovieTopList>
)

data class ApiSearchMovieList(
    val total: Int,
    val items: List<ApiMovieSearchList>
)
