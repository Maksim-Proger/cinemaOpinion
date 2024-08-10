package com.pozmaxpav.cinemaopinion.domain.models

data class Movie(
    val kinopoiskId: Int,
    val nameRu: String,
    val posterUrl: String,
    val posterUrlPreview: String,
    val genres: List<Genre>,
    val premiereRu: String,
    val countries: List<Country>
)

data class MovieTopList(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String,
)
