package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel

interface PersonalMovieRepository {
    fun removeListener()
    suspend fun addMovieToPersonalList(userId: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun getListPersonalMovies(userId: String, ): List<DomainSelectedMovieModel>
    suspend fun observeListSelectedMovies(userId: String, onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit)
    suspend fun deleteMovieFromPersonalList(userId: String, selectedMovieId: Int)
    suspend fun addCommentToMovieInPersonalList(userId: String, selectedMovieId: Int, comment: DomainCommentModel)
    suspend fun getCommentsForMovieFromPersonalList(userId: String, selectedMovieId: Int): List<DomainCommentModel>
    suspend fun observeCommentsForMovieFromPersonalList(userId: String, selectedMovieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)
}