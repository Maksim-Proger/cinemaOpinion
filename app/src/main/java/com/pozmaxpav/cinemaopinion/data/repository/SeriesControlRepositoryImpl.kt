package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.localdb.dao.SeriesControlDao
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.mappers.toEntity
import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.domain.repository.SeriesControlRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeriesControlRepositoryImpl @Inject constructor(
    private val seriesControlDao: SeriesControlDao
): SeriesControlRepository {

    override suspend fun insertNewMovie(movie: SeriesControlModel) {
        seriesControlDao.insertNewMovie(movie.toEntity())
    }

    override suspend fun getListMovies(): Flow<List<SeriesControlModel>> {
        return seriesControlDao.getListMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMovieById(id: Int): SeriesControlModel? {
        return seriesControlDao.getMovieById(id)?.toDomain()
    }

    override suspend fun deleteMovie(movie: SeriesControlModel) {
        seriesControlDao.deleteMovie(movie.toEntity())
    }

}