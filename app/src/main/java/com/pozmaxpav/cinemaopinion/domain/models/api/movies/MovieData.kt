package com.pozmaxpav.cinemaopinion.domain.models.api.movies

import com.pozmaxpav.cinemaopinion.data.models.api.movie.ApiGenre

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
        val genres: List<Genre>,

        override val nameRu: String,
        override val posterUrl: String,
        override val year: String,
        override val countries: List<Country>
    ) : MovieData() {
        override val id: Int get() = filmId
    }

    data class MovieSearch(
        val kinopoiskId: Int,
        val ratingKinopoisk: Double?,
        val ratingImdb: Double?,
        val filmLength: Int?,
        val genres: List<Genre>,

        override val nameRu: String?,
        override val posterUrl: String,
        override val year: String?,
        override val countries: List<Country> = emptyList()
    ) : MovieData() {
        override val id: Int get() = kinopoiskId
    }

    data class MovieSearch2(
        val filmId: Int,
        val nameEn: String?,
        val filmLength: Int?,
        val genres: List<Genre>,
        val rating: Double?,

        override val nameRu: String?,
        override val posterUrl: String,
        override val year: String?,
        override val countries: List<Country> = emptyList()
    ) : MovieData() {
        override val id: Int get() = filmId
    }

}