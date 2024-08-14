package com.pozmaxpav.cinemaopinion.data.api.models

data class ApiMovieSearchList(
    val total: Int,
    val items: List<ApiMovieSearch>
)