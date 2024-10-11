package com.pozmaxpav.cinemaopinion.domain.repository

import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import kotlinx.coroutines.flow.Flow

interface SeriesControlRepository {
    suspend fun insertNewMovie(movie: SeriesControlModel)
    suspend fun getListMovies(): Flow<List<SeriesControlModel>>
    suspend fun getMovieById(id: Int): SeriesControlModel?
    suspend fun deleteMovie(movie: SeriesControlModel)
    suspend fun updateMovie(movie: SeriesControlModel)
}