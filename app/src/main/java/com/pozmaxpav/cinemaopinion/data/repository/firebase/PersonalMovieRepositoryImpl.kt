package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.listeners.FirebaseListenerHolder
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.models.firebase.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_PERSONAL_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import com.pozmaxpav.cinemaopinion.utilits.NODE_PERSONAL_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.COMMENTS_KEY_LISTENER
import com.pozmaxpav.cinemaopinion.utilits.MOVIES_KEY_LISTENER
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalMovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val listenerHolder: FirebaseListenerHolder
) : PersonalMovieRepository {

    override suspend fun addMovieToPersonalList(userId: String, selectedMovie: DomainSelectedMovieModel) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userSnapshot = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val newSelectedMovieId = databaseReference
            .child(NODE_LIST_USERS)
            .child(userSnapshot)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .push().key!!

        databaseReference
            .child(NODE_LIST_USERS)
            .child(userSnapshot)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .child(newSelectedMovieId)
            .setValue(selectedMovie)
            .await()
    }

    override suspend fun getListPersonalMovies(userId: String): List<DomainSelectedMovieModel> {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        return databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .get()
            .await()
            .children.mapNotNull { it.getValue(DomainSelectedMovieModel::class.java) }
    }

    override suspend fun observeListSelectedMovies(
        userId: String,
        onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit
    ) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userSnapshot = snapshot.children.firstOrNull()

                    if (userSnapshot == null) {
                        onSelectedMoviesUpdated(emptyList()) // Если пользователь не найден, вернуть пустой список
                        return
                    }

                    val moviesRef = userSnapshot.child(NODE_LIST_PERSONAL_MOVIES).ref

                    val listener = moviesRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(selectedMovieSnapshot: DataSnapshot) {
                            val selectedMovies = selectedMovieSnapshot.children.mapNotNull {
                                runCatching { it.getValue(DomainSelectedMovieModel::class.java) }.getOrNull()
                            }
                            onSelectedMoviesUpdated(selectedMovies)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Error loading selected movies: ${error.message}")
                        }
                    })
                    listenerHolder.addListener(MOVIES_KEY_LISTENER, listener)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching user data: ${error.message}")
                    onSelectedMoviesUpdated(emptyList()) // Возвращаем пустой список в случае ошибки
                }
            })
    }

    override suspend fun deleteMovieFromPersonalList(userId: String, selectedMovieId: Int) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")
        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val listSelectedMovies = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .get()
            .await()

        listSelectedMovies.takeIf { it.exists() && it.hasChildren() }?.children?.forEach { movie ->
            if (movie.child("id").getValue(Int::class.java) == selectedMovieId) {
                movie.ref.removeValue().await()
                return
            }
        }
    }

    override suspend fun addCommentToMovieInPersonalList(
        userId: String,
        selectedMovieId: Int,
        comment: DomainCommentModel
    ) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val movieKey = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .orderByChild("id")
            .equalTo(selectedMovieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("Movie with ID $selectedMovieId not found")

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
        }
    }

    override suspend fun getCommentsForMovieFromPersonalList(
        userId: String,
        selectedMovieId: Int
    ): List<DomainCommentModel> {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val movieKey = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .orderByChild("id")
            .equalTo(selectedMovieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("Movie with ID $selectedMovieId not found")

        val commentsSnapshot = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .child(movieKey)
            .child(NODE_PERSONAL_COMMENTS)
            .get()
            .await()

        return commentsSnapshot.children.mapNotNull {
            it.getValue(DataComment::class.java)?.commentToDomain()
        }
    }

    override suspend fun observeCommentsForMovieFromPersonalList(
        userId: String,
        selectedMovieId: Int,
        onCommentsUpdated: (List<DomainCommentModel>) -> Unit
    ) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    val userKey = userSnapshot.children.firstOrNull()?.key
                    if (userKey == null) {
                        onCommentsUpdated(emptyList())
                        return
                    }

                    databaseReference
                        .child(NODE_LIST_USERS)
                        .child(userKey)
                        .child(NODE_LIST_PERSONAL_MOVIES)
                        .orderByChild("id")
                        .equalTo(selectedMovieId.toDouble())
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(movieSnapshot: DataSnapshot) {
                                val movieKey = movieSnapshot.children.firstOrNull()?.key
                                if (movieKey == null) {
                                    onCommentsUpdated(emptyList())
                                    return
                                }

                                val commentsRef = databaseReference
                                    .child(NODE_LIST_USERS)
                                    .child(userKey)
                                    .child(NODE_LIST_PERSONAL_MOVIES)
                                    .child(movieKey)
                                    .child(NODE_PERSONAL_COMMENTS)

                                val listener = commentsRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(commentsSnapshot: DataSnapshot) {
                                        val comments = commentsSnapshot.children.mapNotNull {
                                            it.getValue(DataComment::class.java)?.commentToDomain()
                                        }
                                        onCommentsUpdated(comments)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("Firebase", "Error loading comments: ${error.message}")
                                        onCommentsUpdated(emptyList())
                                    }
                                })
                                listenerHolder.addListener(COMMENTS_KEY_LISTENER, listener)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Firebase", "Error fetching movie data: ${error.message}")
                                onCommentsUpdated(emptyList())
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error fetching user data: ${error.message}")
                    onCommentsUpdated(emptyList())
                }
            })
    }

    override fun removeSelectedMoviesListener() {
        listenerHolder.removeListener(MOVIES_KEY_LISTENER)
    }

    override fun removeCommentsSelectedMoviesListener() {
        listenerHolder.removeListener(COMMENTS_KEY_LISTENER)
    }

}