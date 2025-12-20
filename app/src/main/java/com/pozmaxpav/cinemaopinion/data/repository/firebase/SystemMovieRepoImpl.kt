package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SystemMovieRepo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemMovieRepoImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SystemMovieRepo {
    override suspend fun getMovies(dataSource: String): List<DomainSelectedMovieModel> {
        if (dataSource.isEmpty()) throw IllegalArgumentException("The data source cannot be empty")
        val snapshotMovie = databaseReference
            .child(dataSource)
            .get()
            .await()

        return snapshotMovie.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(DomainSelectedMovieModel::class.java)
        }
    }
}