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
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import com.pozmaxpav.cinemaopinion.utilits.COMMENTS_KEY_LISTENER
import com.pozmaxpav.cinemaopinion.utilits.MOVIES_KEY_LISTENER
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_CHANGES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val listenerHolder: FirebaseListenerHolder
) : MovieRepository {

    override suspend fun saveMovie(dataSource: String, selectedMovie: DomainSelectedMovieModel) {
        val ref = databaseReference.child(dataSource).push()
        ref.setValue(selectedMovie).await()
    }
    override suspend fun removeMovie(dataSource: String, movieId: Int) {
        try {
            val snapshot = databaseReference
                .child(dataSource)
                .orderByChild("id")
                .equalTo(movieId.toDouble())
                .get()
                .await()

            snapshot.takeIf { it.exists() && it.hasChildren() }?.children?.forEach { movie ->
                if (movie.child("id").getValue(Int::class.java) == movieId) {
                    movie.ref.removeValue().await()
                    return
                }
            }
        } catch (e: Exception) {
            // TODO: Добавить отлов ошибки
        }
    }
    override suspend fun getMovie(dataSource: String): List<DomainSelectedMovieModel> {
        val generalListMovies = databaseReference
            .child(dataSource)
            .get()
            .await()
        
        return generalListMovies.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(DomainSelectedMovieModel::class.java)
        }
    }
    override suspend fun getMovieById(dataSource: String, movieId: Int): DomainSelectedMovieModel? {
        val movieSnapshot = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get()
            .await()

        if (movieSnapshot.exists()) {
            val movieKey = movieSnapshot.children.firstOrNull()?.key
            if (movieKey != null) {
                val movieData = databaseReference
                    .child(dataSource)
                    .child(movieKey)
                    .get()
                    .await()

                return movieData.getValue(DomainSelectedMovieModel::class.java)
            }
        }
        return null
    }
    override suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        val listener = databaseReference
            .child(dataSource)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movies = snapshot.children.mapNotNull { movieSnapshot ->
                        movieSnapshot.getValue(DomainSelectedMovieModel::class.java)
                    }
                    onMoviesUpdated(movies)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Movies load cancelled: ${error.message}")
                }
            })
        listenerHolder.addListener(MOVIES_KEY_LISTENER, listener)
    }

    override suspend fun addCommentToMovie(dataSource: String, movieId: Double, comment: DomainCommentModel) {
        val snapshot = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId)
            .get()
            .await()

        if (!snapshot.exists()) {
            throw IllegalArgumentException("Movie with ID $movieId not found.")
        }

        val movieKey = snapshot.children.firstOrNull()?.key
            ?: throw IllegalStateException("Movie key not found for ID $movieId.")

        val commentId = databaseReference
            .child(dataSource)
            .child(movieKey)
            .child(NODE_COMMENTS)
            .push()
            .key
            ?: throw IllegalStateException("Failed to generate comment ID.")

        val dataComment = comment.commentToData().copy(commentId = commentId)

        databaseReference
            .child(dataSource)
            .child(movieKey)
            .child(NODE_COMMENTS)
            .child(commentId)
            .setValue(dataComment)
            .await()

    }
    override suspend fun getCommentsForMovie(dataSource: String, movieId: Int): List<DomainCommentModel> {
        val movieSnapshot = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()
            ?: throw IllegalArgumentException("Movie with ID $movieId not found")

        return movieSnapshot
            .child(NODE_COMMENTS)
            .children.mapNotNull { it.getValue(DataComment::class.java)?.commentToDomain() }
    }
    override suspend fun observeCommentsForMovie(dataSource: String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movieSnapshot = snapshot.children.firstOrNull() ?: run {
                        onCommentsUpdated(emptyList())
                        return
                    }

                    val commentsRef = databaseReference
                        .child(dataSource)
                        .child(movieSnapshot.key ?: return)
                        .child(NODE_COMMENTS)

                    val listener = commentsRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(commentsSnapshot: DataSnapshot) {
                            val comments = commentsSnapshot.children.mapNotNull {
                                it.getValue(DataComment::class.java)?.commentToDomain()
                            }
                            onCommentsUpdated(comments)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Comments load error: ${error.message}")
                        }
                    })
                    listenerHolder.addListener(COMMENTS_KEY_LISTENER, listener)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Movie query error: ${error.message}")
                }
            })
    }
    override suspend fun updateComment(dataSource: String, selectedMovieId: Int, commentId: String, selectedComment: DomainCommentModel) {
        val movieKey = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(selectedMovieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("Movie with ID $selectedMovieId not found")

        val commentSnapshot = databaseReference
            .child(dataSource)
            .child(movieKey)
            .child(NODE_COMMENTS)
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

    override suspend fun sendingToNewDirectory(dataSource: String, directionDataSource: String, movieId: Double) {
        try {
            val snapshot = databaseReference
                .child(dataSource)
                .orderByChild("id")
                .equalTo(movieId)
                .get()
                .await()

            if (snapshot.exists()) {
                val movieSnapshot =
                    snapshot.children.firstOrNull() // Берём первую подходящую запись
                val movieKey = movieSnapshot?.key // Получаем ключ записи

                if (movieSnapshot != null && movieKey != null) {
                    val movieData = movieSnapshot.value // Получаем данные записи

                    // Копируем запись в новую папку
                    databaseReference
                        .child(directionDataSource)
                        .child(movieKey)
                        .setValue(movieData)
                        .await()

                    // Удаляем запись после переноса
                    databaseReference
                        .child(dataSource)
                        .child(movieKey)
                        .removeValue()
                        .await()

                    changeRecords(movieId, directionDataSource)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private suspend fun changeRecords(movieId: Double, directionDataSource: String) {
        val snapshot = databaseReference.child(NODE_LIST_CHANGES).get().await()
        snapshot.children.forEach { childSnapshot ->
            val result = childSnapshot.getValue(DomainChangelogModel::class.java)
            if (result?.entityId == movieId.toInt()) {
                val updates = mapOf("newDataSource" to directionDataSource)
                databaseReference.child(NODE_LIST_CHANGES).child(childSnapshot.key!!).updateChildren(updates).await()
            }
        }
    }

    override fun removeSelectedMoviesListener() {
        listenerHolder.removeListener(MOVIES_KEY_LISTENER)
    }
    override fun removeCommentsSelectedMoviesListener() {
        listenerHolder.removeListener(COMMENTS_KEY_LISTENER)
    }

}
