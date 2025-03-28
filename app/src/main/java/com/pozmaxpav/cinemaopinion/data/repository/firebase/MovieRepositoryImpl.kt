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
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import com.pozmaxpav.cinemaopinion.utilits.COMMENTS_KEY_LISTENER
import com.pozmaxpav.cinemaopinion.utilits.MOVIES_KEY_LISTENER
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
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
        val key = databaseReference.child(dataSource).push().key
        key?.let {
            databaseReference
                .child(dataSource)
                .child(it)
                .setValue(selectedMovie)
                .await()
        } ?: throw Exception("Failed to generate key")
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

    override suspend fun observeListMovies(
        dataSource: String,
        onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit
    ) {
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

    override suspend fun addCommentToMovie(
        dataSource: String,
        movieId: Double,
        comment: DomainCommentModel
    ) {
        // Поиск фильма по его ID
        val snapshot = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId)
            .get()
            .await()

        // Проверяем, существует ли фильм с данным ID
        if (snapshot.exists()) {
            // Получаем ключ узла фильма (movieId)
            val movieKey = snapshot.children.firstOrNull()?.key

            // Получаем уникальный ключ для комментария
            val commentId = databaseReference
                .child(dataSource)
                .child(movieKey!!)
                .child(NODE_COMMENTS)
                .push().key!!

            // Преобразуем DomainComment в DataComment
            val dataComment = comment.commentToData().copy(commentId = commentId)

            // Добавляем комментарий под конкретным фильмом
            databaseReference
                .child(dataSource)
                .child(movieKey)
                .child(NODE_COMMENTS)
                .child(commentId)
                .setValue(dataComment)
                .await()
        } else {
            throw IllegalArgumentException("Movie with ID $movieId not found.")
        }
    }

    override suspend fun getCommentsForMovie(
        dataSource: String,
        movieId: Int
    ): List<DomainCommentModel> {
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

    override suspend fun observeCommentsForMovie(
        dataSource: String,
        movieId: Int,
        onCommentsUpdated: (List<DomainCommentModel>) -> Unit
    ) {
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

    override suspend fun sendingToTheViewedFolder(
        dataSource: String,
        directionDataSource: String,
        movieId: Double
    ) {
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
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendingToTheSerialsList(movieId: Double) {
        try {
            val snapshot = databaseReference
                .child(NODE_LIST_MOVIES)
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
                        .child(NODE_LIST_SERIALS)
                        .child(movieKey)
                        .setValue(movieData)
                        .await()

                    // Удаляем запись после переноса
                    databaseReference
                        .child(NODE_LIST_MOVIES)
                        .child(movieKey)
                        .removeValue()
                        .await()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun removeSelectedMoviesListener() {
        listenerHolder.removeListener(MOVIES_KEY_LISTENER)
    }

    override fun removeCommentsSelectedMoviesListener() {
        listenerHolder.removeListener(COMMENTS_KEY_LISTENER)
    }

}
