package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.api.models.ApiCountry
import com.pozmaxpav.cinemaopinion.data.api.models.ApiGenre
import com.pozmaxpav.cinemaopinion.data.api.models.ApiMovie
import com.pozmaxpav.cinemaopinion.data.api.models.ApiMovieList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiMovieSearchList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiMovieTopList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiPagedMovieList
import com.pozmaxpav.cinemaopinion.data.api.models.ApiSearchMovieList
import com.pozmaxpav.cinemaopinion.domain.models.Country
import com.pozmaxpav.cinemaopinion.domain.models.Genre
import com.pozmaxpav.cinemaopinion.domain.models.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.PagedMovieList
import com.pozmaxpav.cinemaopinion.domain.models.SearchList
import com.pozmaxpav.cinemaopinion.domain.models.SearchListMovie

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
        year = year,
        countries = countries.map { it.toDomain() }
    )
}

fun ApiMovieTopList.toDomain(): MovieData.MovieTopList {
    return MovieData.MovieTopList(
        filmId = filmId,
        rating = rating,
        nameRu = nameRu,
        posterUrl = posterUrl,
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

fun ApiPagedMovieList.toDomain(): PagedMovieList {
    return PagedMovieList(
        pagesCount = pagesCount,
        films = films.map { it.toDomain() }
    )
}

fun ApiMovieSearchList.toDomain2(): SearchListMovie {
    return SearchListMovie(
        kinopoiskId = kinopoiskId,
        nameRu = nameRu,
        posterUrl = posterUrl
    )
}

fun ApiSearchMovieList.toDomain2(): SearchList {
    return SearchList(
        total = total,
        items = items.map { it.toDomain2() }
    )
}
