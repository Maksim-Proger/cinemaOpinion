package com.pozmaxpav.cinemaopinion.data.models.api.movie

data class ApiMovieList(
    val total: Int,
    val items: List<ApiMovie>
)
