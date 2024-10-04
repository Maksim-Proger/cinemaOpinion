package com.pozmaxpav.cinemaopinion.data.mappers

import com.pozmaxpav.cinemaopinion.data.api.models.modelfirebase.FilmData
import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain

fun FilmData.toDomain(): FilmDomain {
    return FilmDomain(
        titleFilm = titleFilm
    )
}

fun FilmDomain.toData(): FilmData {
    return FilmData(
        titleFilm = titleFilm
    )
}