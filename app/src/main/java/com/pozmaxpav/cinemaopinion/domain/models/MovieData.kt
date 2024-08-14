package com.pozmaxpav.cinemaopinion.domain.models

sealed class MovieData {
    abstract val nameRu: String?
    abstract val posterUrl: String?
    abstract val year: String?
    abstract val countries: List<Country>

    data class Movie(
        val kinopoiskId: Int,
        val posterUrlPreview: String,
        val genres: List<Genre>,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData()

    data class MovieTop(
        val filmId: Int,
        val rating: String,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData()

    data class MovieSearch(
        val kinopoiskId: Int,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData()

}