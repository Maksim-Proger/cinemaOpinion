package com.pozmaxpav.cinemaopinion.data.source

import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData

interface PremieresSource {
    suspend fun getPremieres(year: Int, month: Int): List<MovieData.Movie>
}