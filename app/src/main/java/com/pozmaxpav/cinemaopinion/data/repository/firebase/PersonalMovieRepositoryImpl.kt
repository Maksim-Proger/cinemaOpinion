package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.example.core.utils.CoreDatabaseConstants.COMMENTS_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.MOVIES_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_CHANGES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_PERSONAL_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.example.core.utils.CoreDatabaseConstants.NODE_PERSONAL_COMMENTS
import com.example.core.utils.FirebaseListenerHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.models.firebase.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.PersonalMovieRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalMovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val listenerHolder: FirebaseListenerHolder
) : PersonalMovieRepository {

    override suspend fun addMovie(userId: String, selectedMovie: DomainSelectedMovieModel) {
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
    override suspend fun deleteMovie(userId: String, selectedMovieId: Int) {
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
    override suspend fun getMovies(userId: String): List<DomainSelectedMovieModel> {
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
    override suspend fun observeListMovies(userId: String, onSelectedMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val moviesRef = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movies = snapshot.children.mapNotNull {
                    runCatching { it.getValue(DomainSelectedMovieModel::class.java) }.getOrNull()
                }
                onSelectedMoviesUpdated(movies)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error loading selected movies: ${error.message}")
            }
        }

        // 4. Навешиваем listener на moviesRef
        moviesRef.addValueEventListener(listener)
        listenerHolder.addListener(MOVIES_KEY_LISTENER, moviesRef, listener)
    }

    override suspend fun addComment(userId: String, selectedMovieId: Int, comment: DomainCommentModel) {
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
    override suspend fun getComments(userId: String, selectedMovieId: Int): List<DomainCommentModel> {
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
    override suspend fun observeListComments(userId: String, selectedMovieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        // 1. Находим userKey
        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID $userId not found")

        // 2. Находим movieKey среди personal movies
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

        // 3. Ссылка на комментарии
        val commentsRef = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .child(movieKey)
            .child(NODE_PERSONAL_COMMENTS)

        // 4. Создаем отдельный listener
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = snapshot.children.mapNotNull {
                    it.getValue(DataComment::class.java)?.commentToDomain()
                }
                onCommentsUpdated(comments)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error loading comments: ${error.message}")
                onCommentsUpdated(emptyList())
            }
        }

        // 5. Навешиваем на DatabaseReference
        commentsRef.addValueEventListener(listener)

        // 6. Сохраняем в holder
        listenerHolder.addListener(COMMENTS_KEY_LISTENER, commentsRef, listener)
    }
    override suspend fun updateComment(userId: String, selectedMovieId: Int, commentId: String, selectedComment: DomainCommentModel) {
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

        val commentSnapshot = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child(NODE_LIST_PERSONAL_MOVIES)
            .child(movieKey)
            .child(NODE_PERSONAL_COMMENTS)
            .orderByChild("commentId")
            .equalTo(commentId)
            .get()
            .await()
            ?: throw IllegalArgumentException("Comment with ID $commentId not found")

        for (comment in commentSnapshot.children) {
            if (comment.child("commentId").getValue(String::class.java) == commentId) {
                comment.ref.setValue(selectedComment).await()
                break
            }
        }

    }

//    override suspend fun sendingToNewDirectory(userId: String, dataSource: String, directionDataSource: String, selectedMovieId: Int) {
//        try {
//            if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")
//
//            val userKey = databaseReference
//                .child(NODE_LIST_USERS)
//                .orderByChild("id")
//                .equalTo(userId)
//                .get()
//                .await()
//                .children.firstOrNull()?.key
//                ?: throw IllegalArgumentException("User with ID $userId not found.")
//
//            val movieKey = databaseReference
//                .child(NODE_LIST_USERS)
//                .child(userKey)
//                .child(NODE_LIST_PERSONAL_MOVIES)
//                .orderByChild("id")
//                .equalTo(selectedMovieId.toDouble())
//                .get()
//                .await()
//                .children.firstOrNull()?.key
//                ?: throw IllegalArgumentException("Movie with ID $selectedMovieId not found")
//
//            val movieSnapshot = databaseReference
//                .child(NODE_LIST_USERS)
//                .child(userKey)
//                .child(NODE_LIST_PERSONAL_MOVIES)
//                .child(movieKey)
//                .get()
//                .await()
//                ?: throw IllegalArgumentException("Movie with ID $selectedMovieId not found")
//
//            val movieData = movieSnapshot.value // Получаем данные записи
//
//            // Копируем запись в новую папку
//            databaseReference
//                .child(directionDataSource)
//                .child(movieKey)
//                .setValue(movieData)
//                .await()
//
//            // Удаляем запись после переноса
//            databaseReference
//                .child(NODE_LIST_USERS)
//                .child(userKey)
//                .child(NODE_LIST_PERSONAL_MOVIES)
//                .child(movieKey)
//                .removeValue()
//                .await()
//
//            changeRecords(selectedMovieId.toDouble(), directionDataSource)
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//    private suspend fun changeRecords(movieId: Double, directionDataSource: String) {
//        val snapshot = databaseReference.child(NODE_LIST_CHANGES).get().await()
//        snapshot.children.forEach { childSnapshot ->
//            val result = childSnapshot.getValue(DomainNotificationModel::class.java)
//            if (result?.entityId == movieId.toInt()) {
//                val updates = mapOf("newDataSource" to directionDataSource)
//                databaseReference.child(NODE_LIST_CHANGES).child(childSnapshot.key!!)
//                    .updateChildren(updates).await()
//            }
//        }
//    }

    override fun removeMoviesListener() {
        listenerHolder.removeListener(MOVIES_KEY_LISTENER)
    }
    override fun removeCommentsListener() {
        listenerHolder.removeListener(COMMENTS_KEY_LISTENER)
    }
}