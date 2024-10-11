package com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi

data class ApiMovieSearchList(
    val total: Int,
    val items: List<ApiMovieSearch>
)