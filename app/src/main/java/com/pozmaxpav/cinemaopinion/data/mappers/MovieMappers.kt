package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiCountry
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiGenre
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovie
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovieList
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovieSearch
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovieTop
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovieTopList
import com.pozmaxpav.cinemaopinion.data.api.models.moviemodelsapi.ApiMovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.Country
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.Genre
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieSearchList

fun ApiGenre.toDomain(): Genre {
    return Genre(
        genre = genre
    )
}

fun ApiCountry.toDomain(): Country {
    return Country(
        country = country
    )
}

fun ApiMovie.toDomain(): MovieData.Movie {
    return MovieData.Movie(
        kinopoiskId = kinopoiskId,
        nameRu = nameRu,
        posterUrl = posterUrl,
        posterUrlPreview = posterUrlPreview,
        genres = genres.map { it.toDomain() },
        premiereRu = premiereRu,
        year = year,
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieList.toDomain(): MovieList {
    return MovieList(
        total = total,
        items = items.map { it.toDomain() }
    )
}

fun ApiMovieTop.toDomain(): MovieData.MovieTop {
    return MovieData.MovieTop(
        filmId = filmId,
        rating = rating,
        nameRu = nameRu,
        posterUrl = posterUrl,
        year = year,
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieTopList.toDomain(): MovieTopList {
    return MovieTopList(
        pagesCount = pagesCount,
        films = films.map { it.toDomain() }
    )
}

fun ApiMovieSearch.toDomain(): MovieData.MovieSearch {
    return MovieData.MovieSearch(
        kinopoiskId = kinopoiskId,
        nameRu = nameRu,
        posterUrl = posterUrl,
        year = year,
        ratingKinopoisk = ratingKinopoisk,
        ratingImdb = ratingImdb,
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieSearchList.toDomain(): MovieSearchList {
    return MovieSearchList(
        totalPages = totalPages,
        items = items.map { it.toDomain() }
    )
}
