package com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase

import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment

interface FirebaseRepository {
    suspend fun saveMovie(selectedMovie: SelectedMovie)
    suspend fun removeMovie(id: Double)
    suspend fun getMovie(): List<SelectedMovie>
    suspend fun addCommentToMovie(movieId: Double, comment: DomainComment)
    suspend fun getCommentsForMovie(movieId: Double): List<DomainComment>
    suspend fun observeCommentsForMovie(movieId: Double, onCommentsUpdated: (List<DomainComment>) -> Unit)
    suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel)
    suspend fun getRecordsOfChanges(): List<DomainChangelogModel>
    suspend fun removeRecordsOfChanges(id: String)
}