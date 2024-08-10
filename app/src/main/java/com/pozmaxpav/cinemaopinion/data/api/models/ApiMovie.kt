package com.pozmaxpav.cinemaopinion.data.api.models

data class ApiMovie(
    val kinopoiskId: Int,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val genres: List<ApiGenre>,
    val premiereRu: String,
    val countries: List<ApiCountry>
)

data class ApiMovieTopList(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String,
)
