package com.pozmaxpav.cinemaopinion.data.api.models

import com.pozmaxpav.cinemaopinion.domain.models.Country

data class ApiMovie(
    val kinopoiskId: Int,
    val posterUrlPreview: String,
    val genres: List<ApiGenre>,
    val nameRu: String,
    val posterUrl: String,
    val year: String,
    val countries: List<ApiCountry>
)

data class ApiMovieTop(
    val filmId: Int,
    val rating: String,
    val nameRu: String,
    val posterUrl: String,
    val year: String,
    val countries: List<ApiCountry>
)

data class ApiMovieSearch(
    val kinopoiskId: Int,
    val nameRu: String,
    val posterUrl: String,
    val year: String,
    val countries: List<ApiCountry>
)
