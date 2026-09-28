package com.pozmaxpav.cinemaopinion.data.source

import com.example.backend.PremiereItemDto
import com.example.backend.domain.BackendGetPremieresUseCase
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Country
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Genre
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData

class BackendPremieresSource(
    private val backendGetPremieresUseCase: BackendGetPremieresUseCase
) : PremieresSource {

    override suspend fun getPremieres(year: Int, month: Int): List<MovieData.Movie> =
        backendGetPremieresUseCase(year, month).getOrThrow().mapNotNull { it.toDomain() }
}

private fun PremiereItemDto.toDomain(): MovieData.Movie? {
    val id = kpId ?: return null
    return MovieData.Movie(
        kinopoiskId = id,
        nameRu = titleRu.orEmpty(),
        posterUrl = posterUrl.orEmpty(),
        posterUrlPreview = posterPreview.orEmpty(),
        genres = genres.orEmpty().map { Genre(it) },
        premiereRu = premiereRu.orEmpty(),
        year = year?.toString().orEmpty(),
        countries = countries.orEmpty().map { Country(it) }
    )
}