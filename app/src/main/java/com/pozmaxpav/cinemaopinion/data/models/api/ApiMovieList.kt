package com.pozmaxpav.cinemaopinion.data.models.api

data class ApiMovieList(
    val total: Int,
    val items: List<ApiMovie>
)
