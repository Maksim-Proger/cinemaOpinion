package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel

interface MovieRepository {
    suspend fun saveMovie(dataSource: String, selectedMovie: SelectedMovieModel)
    suspend fun removeMovie(dataSource: String, id: Double)
    suspend fun getMovie(dataSource: String): List<SelectedMovieModel>
    suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<SelectedMovieModel>) -> Unit)

    suspend fun addCommentToMovie(dataSource: String, movieId: Double, comment: DomainCommentModel)
    suspend fun getCommentsForMovie(dataSource: String, movieId: Double): List<DomainCommentModel>
    suspend fun observeCommentsForMovie(dataSource:String, movieId: Double, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)

    suspend fun sendingToTheViewedFolder(dataSource: String, directionDataSource: String, movieId: Double)
    suspend fun sendingToTheSerialsList(movieId: Double)
}