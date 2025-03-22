package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SelectedMovieRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_PERSONAL_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SelectedMovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SelectedMovieRepository {

    private var valueEventListener: ValueEventListener? = null
    override fun removeListener() {
        valueEventListener?.let { listener ->
            databaseReference.removeEventListener(listener)
            valueEventListener = null
        }
    }

    override suspend fun addMovieToPersonalList(userId: String, selectedMovie: SelectedMovieModel) {
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                val newSelectedMovieId = databaseReference
                    .child(NODE_LIST_USERS)
                    .child(userKey!!)
                    .child(NODE_LIST_PERSONAL_MOVIES)
                    .push().key!!

                databaseReference
                    .child(NODE_LIST_USERS)
                    .child(userKey)
                    .child(NODE_LIST_PERSONAL_MOVIES)
                    .child(newSelectedMovieId)
                    .setValue(selectedMovie)
                    .await()
            } else {
                throw IllegalArgumentException("User with ID $userId not found.")
            }
        }
    }

    override suspend fun getListPersonalMovies(userId: String): List<SelectedMovieModel> {
        val selectedMoviesList = mutableListOf<SelectedMovieModel>()
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                if (userKey != null) {
                    val selectedMoviesSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_LIST_PERSONAL_MOVIES)
                        .get()
                        .await()

                    for (selectedMovie in selectedMoviesSnapshot.children) {
                        val movie = selectedMovie.getValue(SelectedMovieModel::class.java)
                        if (movie != null) {
                            selectedMoviesList.add(movie)
                        }
                    }
                }
            } else {
                throw IllegalArgumentException("User with ID $userId not found.")
            }
        }
        return selectedMoviesList
    }

    override suspend fun observeListSelectedMovies(
        userId: String,
        onSelectedMoviesUpdated: (List<SelectedMovieModel>) -> Unit
    ) {
        removeListener()
        if (userId.isNotEmpty()) {
            databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userSnapshot = snapshot.children.firstOrNull()
                        valueEventListener = userSnapshot?.child(NODE_LIST_PERSONAL_MOVIES)?.ref?.addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(selectedMovieSnapshot: DataSnapshot) {
                                val selectedMovie = selectedMovieSnapshot.children.mapNotNull {
                                    it.getValue(
                                        SelectedMovieModel::class.java
                                    )
                                }
                                onSelectedMoviesUpdated(selectedMovie)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    override suspend fun deleteMovieFromPersonalList(userId: String, selectedMovieId: Int) {
        if (userId.isNotEmpty()) {
            val userSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("id")
                .equalTo(userId)
                .get()
                .await()

            if (userSnapshot.exists()) {
                val userKey = userSnapshot.children.firstOrNull()?.key

                if (userKey != null) {
                    val selectedMoviesSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_LIST_PERSONAL_MOVIES)
                        .get()
                        .await()

                    if (selectedMoviesSnapshot.exists() && selectedMoviesSnapshot.hasChildren()) {
                        for (movie in selectedMoviesSnapshot.children) {
                            if (movie.child("id").getValue(Int::class.java) == selectedMovieId) {
                                movie.ref.removeValue().await()
                                break
                            }
                        }
                    }
                }
            }
        }
    }

}