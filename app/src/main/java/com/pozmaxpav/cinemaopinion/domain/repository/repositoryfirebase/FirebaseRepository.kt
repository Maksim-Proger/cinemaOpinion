package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment

interface FirebaseRepository {
    suspend fun saveMovie(dataSource: String, selectedMovie: SelectedMovie)
    suspend fun removeMovie(id: Double)
    suspend fun getMovie(dataSource: String): List<SelectedMovie>
    suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<SelectedMovie>) -> Unit)
    suspend fun addCommentToMovie(movieId: Double, comment: DomainComment)
    suspend fun getCommentsForMovie(dataSource: String, movieId: Double): List<DomainComment>
    suspend fun observeCommentsForMovie(dataSource:String, movieId: Double, onCommentsUpdated: (List<DomainComment>) -> Unit)
    suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel)
    suspend fun getRecordsOfChanges(): List<DomainChangelogModel>
    suspend fun removeRecordsOfChanges(id: String)
    suspend fun sendingToTheViewedFolder(movieId: Double)
    suspend fun sendingToTheSerialsList(movieId: Double)
    suspend fun updatingUserData(domainUser: DomainUser)
    suspend fun updateSeasonalEventPoints(userId: String, fieldName: String, newValue: Any)
}