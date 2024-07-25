package com.pozmaxpav.cinemaopinion.data.api.models

data class ApiPagedMovieList(
    val pagesCount: Int,
    val films: List<ApiMovie>
)
