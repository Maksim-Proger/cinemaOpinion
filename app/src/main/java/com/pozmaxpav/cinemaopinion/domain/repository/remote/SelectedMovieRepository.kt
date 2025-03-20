package com.pozmaxpav.cinemaopinion.domain.repository.remote

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel

interface SelectedMovieRepository {
    fun removeListener()
    suspend fun addMovieToPersonalList(userId: String, selectedMovie: SelectedMovieModel)
    suspend fun getListPersonalMovies(userId: String, ): List<SelectedMovieModel>
    suspend fun observeListSelectedMovies(
        userId: String, onSelectedMoviesUpdated: (List<SelectedMovieModel>) -> Unit
    )
    suspend fun deleteMovieFromPersonalList(userId: String, selectedMovieId: Int)
}