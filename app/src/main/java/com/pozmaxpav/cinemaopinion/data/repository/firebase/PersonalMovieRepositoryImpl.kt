package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_PERSONAL_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import com.pozmaxpav.cinemaopinion.utilits.NODE_PERSONAL_COMMENTS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PersonalMovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : PersonalMovieRepository {

    private var valueEventListener: ValueEventListener? = null
    override fun removeListener() {
        valueEventListener?.let { listener ->
            databaseReference.removeEventListener(listener)
            valueEventListener = null
        }
    }

    override suspend fun addMovieToPersonalList(userId: String, selectedMovie: DomainSelectedMovieModel) {
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

    override suspend fun getListPersonalMovies(userId: String): List<DomainSelectedMovieModel> {
        val selectedMoviesList = mutableListOf<DomainSelectedMovieModel>()
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
                        val movie = selectedMovie.getValue(DomainSelectedMovieModel::class.java)
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
        onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit
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
                                        DomainSelectedMovieModel::class.java
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

    override suspend fun addCommentToMovieInPersonalList(
        userId: String,
        selectedMovieId: Int,
        comment: DomainCommentModel
    ) {
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
                    val selectedMovieSnapshot = databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_LIST_PERSONAL_MOVIES)
                        .orderByChild("id")
                        .equalTo(selectedMovieId.toDouble())
                        .get()
                        .await()

                    if (selectedMovieSnapshot.exists()) {
                        val movieKey = selectedMovieSnapshot.children.firstOrNull()?.key

                        if (movieKey != null) {
                            val commentId = databaseReference
                                .child(NODE_LIST_USERS)
                                .child(userKey)
                                .child(NODE_LIST_PERSONAL_MOVIES)
                                .child(movieKey)
                                .child(NODE_PERSONAL_COMMENTS)
                                .push().key

                            if (commentId != null) {
                                val dataComment = comment.commentToData().copy(commentId = commentId)
                                databaseReference
                                    .child(NODE_LIST_USERS)
                                    .child(userKey)
                                    .child(NODE_LIST_PERSONAL_MOVIES)
                                    .child(movieKey)
                                    .child(NODE_PERSONAL_COMMENTS)
                                    .child(commentId)
                                    .setValue(dataComment)
                                    .await()
                            } else {
                                throw IllegalStateException("Failed to generate comment ID")
                            }
                        }
                    } else {
                        throw IllegalArgumentException("Movie not found")
                    }
                }
            } else {
                throw IllegalArgumentException("User not found")
            }
        } else {
            throw IllegalArgumentException("User ID cannot be empty")
        }
    }

    override suspend fun getCommentsForMovieFromPersonalList(
        userId: String,
        selectedMovieId: Int
    ): List<DomainCommentModel> {
        TODO("Not yet implemented")
    }

    override suspend fun observeCommentsForMovieFromPersonalList(
        userId: String,
        selectedMovieId: Int,
        onCommentsUpdated: (List<DomainCommentModel>) -> Unit
    ) {
        TODO("Not yet implemented")
    }


}