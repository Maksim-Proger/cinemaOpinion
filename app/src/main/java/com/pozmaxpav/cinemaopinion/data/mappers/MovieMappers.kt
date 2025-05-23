package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.models.api.ApiCountry
import com.pozmaxpav.cinemaopinion.data.models.api.ApiGenre
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovie
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieList
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearch
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearch2
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieTop
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieTopList
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearchList
import com.pozmaxpav.cinemaopinion.data.models.api.ApiMovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Country
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Genre
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2

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
        filmLength = filmLength,
        genres = genres.map { it },
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieSearch2.toDomain(): MovieData.MovieSearch2 {
    return MovieData.MovieSearch2(
        filmId = filmId,
        nameEn = nameEn,
        nameRu = nameRu,
        posterUrl = posterUrl,
        year = year,
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieSearchList.toDomain(): MovieSearchList {
    return MovieSearchList(
        totalPages = totalPages,
        items = items.map { it.toDomain() }
    )
}

fun ApiMovieSearchList2.toDomain(): MovieSearchList2 {
    return MovieSearchList2(
        pagesCount = pagesCount,
        films = films.map { it.toDomain() }
    )
}
