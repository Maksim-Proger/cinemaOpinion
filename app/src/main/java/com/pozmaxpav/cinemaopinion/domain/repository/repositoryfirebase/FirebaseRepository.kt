package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment

interface FirebaseRepository {
    suspend fun saveMovie(dataSource: String, selectedMovie: SelectedMovie)
    suspend fun removeMovie(dataSource: String, id: Double)
    suspend fun getMovie(dataSource: String): List<SelectedMovie>
    suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<SelectedMovie>) -> Unit)
    suspend fun addCommentToMovie(dataSource: String, movieId: Double, comment: DomainComment)
    suspend fun getCommentsForMovie(dataSource: String, movieId: Double): List<DomainComment>
    suspend fun observeCommentsForMovie(dataSource:String, movieId: Double, onCommentsUpdated: (List<DomainComment>) -> Unit)
    suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel)
    suspend fun getRecordsOfChanges(): List<DomainChangelogModel>
    suspend fun removeRecordsOfChanges(id: String)
    suspend fun sendingToTheViewedFolder(dataSource: String, directionDataSource: String, movieId: Double)
    suspend fun sendingToTheSerialsList(movieId: Double)
    suspend fun getUsers(): List<User>
    suspend fun usersScreen(userId: String): User?
    suspend fun addUser(user: User)
    suspend fun checkLoginAndPassword(email: String, password: String): User?
    suspend fun updatingUserData(user: User)
    suspend fun updateSeasonalEventPoints(userId: String, fieldName: String, newValue: Any)
}