package com.pozmaxpav.cinemaopinion.data.repository

import com.pozmaxpav.cinemaopinion.data.localdb.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.mappers.toEntity
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.SelectedMovieRepository
import javax.inject.Inject

class SelectedMovieRepositoryImpl @Inject constructor(
    private val selectedMovieDao: SelectedMovieDao
) : SelectedMovieRepository {
    override suspend fun insertFilm(selectedMovie: SelectedMovie) {
        selectedMovieDao.insertFilm(selectedMovie.toEntity())
    }

    override suspend fun getFilmById(id: Int): SelectedMovie? {
        return selectedMovieDao.getFilmById(id)?.toDomain()
    }

    override suspend fun getListSelectedFilms(): List<SelectedMovie> {
        return selectedMovieDao.getListSelectedFilms().toDomain()
    }

    override suspend fun deleteFilm(selectedMovie: SelectedMovie) {
        selectedMovieDao.deleteFilm(selectedMovie.toEntity())
    }
}