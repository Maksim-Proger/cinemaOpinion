package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

interface SharedListsRepository {

    suspend fun addMovieToSpecificSharedList(listId: String, selectedMovie: DomainSelectedMovieModel)
    suspend fun getMovieFromSpecificSharedList(listId: String) : List<DomainSelectedMovieModel>
    suspend fun addCommentToMovie(listId: String, movieId: Int, comment: DomainCommentModel)
    suspend fun getCommentsForMovie(listId: String, movieId: Int): List<DomainCommentModel>

    suspend fun createSharedList(
        newList: DomainSharedListModel,
        forProfile: DomainMySharedListModel,
        userCreatorId: String,
        invitedUserAddress: String
    )
    
    suspend fun getSharedLists(userId: String) : List<DomainSharedListModel>
}