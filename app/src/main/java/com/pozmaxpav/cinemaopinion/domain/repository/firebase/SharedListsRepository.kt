package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

interface SharedListsRepository {
    // TODO: Проверить методы!
//    suspend fun getMovieById(listId: String, movieId: Int): DomainSelectedMovieModel?
//    suspend fun sendingToNewDirectory(dataSource: String, directionDataSource: String, movieId: Double)



    suspend fun addMovie(listId: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun removeMovie(listId: String, movieId: Int)
    suspend fun getMovies(listId: String): List<DomainSelectedMovieModel>
    suspend fun observeListMovies(listId: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit)

    suspend fun addComment(listId: String, movieId: Int, comment: DomainCommentModel)
    suspend fun updateComment(listId: String, movieId: Int, commentId: String, selectedComment: DomainCommentModel)
    suspend fun getComments(listId: String, movieId: Int): List<DomainCommentModel>
    suspend fun observeListComments(listId:String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit)

    suspend fun createList(newList: DomainSharedListModel, forProfile: DomainMySharedListModel, userCreatorId: String, invitedUserAddress: List<String>)
    suspend fun removeList(listId: String)
    suspend fun getLists(userId: String): List<DomainSharedListModel>
    suspend fun getListName(listId: String): String
    suspend fun observeLists(userId: String, onListsUpdated: (List<DomainSharedListModel>) -> Unit)

    fun removeMoviesListener()
    fun removeCommentsListener()
    fun removeListsListener()
}