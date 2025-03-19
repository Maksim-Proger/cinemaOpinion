package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.repository.remote.SelectedMovieRepository
import javax.inject.Inject

class SelectedMovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SelectedMovieRepository {

    override suspend fun addMovieToPersonalList(selectedMovie: SelectedMovie) {
        TODO("Not yet implemented")
    }

    override suspend fun getListPersonalMovies(): List<SelectedMovie> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMovieFromPersonalList(selectedMovie: SelectedMovie) {
        TODO("Not yet implemented")
    }

}