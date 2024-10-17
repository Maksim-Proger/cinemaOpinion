package com.pozmaxpav.cinemaopinion.domain.models.moviemodels

sealed class MovieData : UnifyingId {
    abstract val nameRu: String?
    abstract val posterUrl: String?
    abstract val year: String?
    abstract val countries: List<Country>

    data class Movie(
        val kinopoiskId: Int,
        val posterUrlPreview: String,
        val genres: List<Genre>,
        val premiereRu: String,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData() {
        override val id: Int get() = kinopoiskId
    }

    data class MovieTop(
        val filmId: Int,
        val rating: String,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData() {
        override val id: Int get() = filmId
    }

    data class MovieSearch(
        val kinopoiskId: Int,
        val ratingKinopoisk: String?,
        val ratingImdb: String?,

        override val nameRu: String?,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData() {
        override val id: Int get() = kinopoiskId
    }

}