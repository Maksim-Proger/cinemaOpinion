package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel

interface MovieRepository {
    fun removeMoviesListener()
    fun removeCommentsListener()

    suspend fun saveMovie(dataSource: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun removeMovie(dataSource: String, movieId: Int)
    suspend fun getMovie(dataSource: String): List<DomainSelectedMovieModel>
    suspend fun getMovieById(dataSource: String, movieId: Int): DomainSelectedMovieModel?
    suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit)

    suspend fun addComment(dataSource: String, movieId: Double, comment: DomainCommentModel)
    suspend fun getComments(dataSource: String, movieId: Int): List<DomainCommentModel>
    suspend fun observeListComments(dataSource:String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)
    suspend fun updateComment(dataSource: String, selectedMovieId: Int, commentId: String, selectedComment: DomainCommentModel)

    suspend fun sendingToNewDirectory(dataSource: String, directionDataSource: String, movieId: Double)
}