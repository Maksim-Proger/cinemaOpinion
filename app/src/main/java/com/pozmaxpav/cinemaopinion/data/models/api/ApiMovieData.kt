package com.pozmaxpav.cinemaopinion.data.models.api

data class ApiMovie(
    val kinopoiskId: Int,
    val posterUrlPreview: String,
    val genres: List<ApiGenre>,
    val premiereRu: String,
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
    val ratingKinopoisk: Double?,
    val ratingImdb: Double?,
    val nameRu: String?,
    val posterUrl: String,
    val year: String?,
    val countries: List<ApiCountry>
)

data class ApiMovieSearch2(
    val filmId: Int,
//    val rating: Double?,
    val nameRu: String?,
    val nameEn: String?,
    val year: String?,
    val countries: List<ApiCountry>,
    val posterUrl: String
)

