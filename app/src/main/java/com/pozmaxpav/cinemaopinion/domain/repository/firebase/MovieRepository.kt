package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel

interface MovieRepository {
    suspend fun saveMovie(dataSource: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun removeMovie(dataSource: String, movieId: Int)
    suspend fun getMovie(dataSource: String): List<DomainSelectedMovieModel>
    suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit)

    suspend fun addCommentToMovie(dataSource: String, movieId: Double, comment: DomainCommentModel)
    suspend fun getCommentsForMovie(dataSource: String, movieId: Int): List<DomainCommentModel>
    suspend fun observeCommentsForMovie(dataSource:String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)

    suspend fun sendingToTheViewedFolder(dataSource: String, directionDataSource: String, movieId: Double)
    suspend fun sendingToTheSerialsList(movieId: Double)
}