package com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi

data class ApiMovieSearchList(
    val totalPages: Int,
    val items: List<ApiMovieSearch>
)