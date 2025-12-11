package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel

interface PersonalMovieRepository {
    fun removeMoviesListener()
    fun removeCommentsListener()

    suspend fun addMovie(userId: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun getMovies(userId: String, ): List<DomainSelectedMovieModel>
    suspend fun observeListMovies(userId: String, onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit)
    suspend fun deleteMovie(userId: String, selectedMovieId: Int)

    suspend fun addComment(userId: String, selectedMovieId: Int, comment: DomainCommentModel)
    suspend fun getComments(userId: String, selectedMovieId: Int): List<DomainCommentModel>
    suspend fun observeListComments(userId: String, selectedMovieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)
    suspend fun updateComment(userId: String, selectedMovieId: Int, commentId: String, selectedComment: DomainCommentModel)

//    suspend fun sendingToNewDirectory(userId: String, dataSource: String, directionDataSource: String, selectedMovieId: Int)
}