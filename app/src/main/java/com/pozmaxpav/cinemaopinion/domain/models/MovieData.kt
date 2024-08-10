package com.pozmaxpav.cinemaopinion.domain.models

sealed class MovieData {
    abstract val nameRu: String
    abstract val posterUrl: String

    data class Movie(
        val kinopoiskId: Int,
        override val nameRu: String,
        override val posterUrl: String,
        val posterUrlPreview: String,
        val genres: List<Genre>,
        val premiereRu: String,
        val countries: List<Country>
    ) : MovieData()

    data class MovieTopList(
        val filmId: Int,
        override val nameRu: String,
        override val posterUrl: String,
    ) : MovieData()

}