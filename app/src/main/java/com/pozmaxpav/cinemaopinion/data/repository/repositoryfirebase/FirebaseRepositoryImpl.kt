package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.firebase.mappers.toData
import com.pozmaxpav.cinemaopinion.data.firebase.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_CHANGES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_WATCHED_MOVIES
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : FirebaseRepository {

    override suspend fun saveMovie(selectedMovie: SelectedMovie) {
        val filmData = SelectedMovie(
            selectedMovie.id,
            selectedMovie.nameFilm,
            selectedMovie.posterUrl,
        )
        val key = databaseReference.child("list_movies").push().key
        key?.let {
            databaseReference.child("list_movies").child(it).setValue(filmData).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun removeMovie(id: Double) {
        try {
            val snapshot =
                databaseReference
                    .child("list_movies")
                    .orderByChild("id")
                    .equalTo(id)
                    .get()
                    .await()

            if (snapshot.exists() && snapshot.hasChildren()) {
                // Проходим по всем найденным элементам
                for (filmSnapshot in snapshot.children) {
                    filmSnapshot.ref.removeValue().await()
                }
            }
        } catch (e: Exception) {
            Log.e("RemoveMovie", "Error: ${e.message}")
        }
    }

    override suspend fun getMovie(dataSource: String): List<SelectedMovie> {
        val snapshot = databaseReference.child(dataSource).get().await()
        return snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(SelectedMovie::class.java)
        }
            .map {
                SelectedMovie(
                    id = it.id,
                    nameFilm = it.nameFilm,
                    posterUrl = it.posterUrl
                )
            }
    }

    override suspend fun observeListMovies(dataSource: String, onMoviesUpdated: (List<SelectedMovie>) -> Unit) {
        databaseReference
            .child(dataSource)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movies = snapshot.children.mapNotNull { movieSnapshot ->
                        movieSnapshot.getValue(SelectedMovie::class.java)
                    }
                    onMoviesUpdated(movies)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseRepositoryImpl", "Error fetching movies: ${error.message}")
                }
            })
    }

    override suspend fun addCommentToMovie(movieId: Double, comment: DomainComment) {
        // Поиск фильма по его ID
        val snapshot = databaseReference
            .child(NODE_LIST_MOVIES)
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
                .child(NODE_LIST_MOVIES)
                .child(movieKey!!)
                .child(NODE_COMMENTS)
                .push().key!!

            // Преобразуем DomainComment в DataComment
            val dataComment = comment.toData().copy(commentId = commentId)

            // Добавляем комментарий под конкретным фильмом
            databaseReference
                .child(NODE_LIST_MOVIES)
                .child(movieKey)
                .child(NODE_COMMENTS)
                .child(commentId)
                .setValue(dataComment)
                .await()
        } else {
            throw IllegalArgumentException("Movie with ID $movieId not found.")
        }
    }

    override suspend fun getCommentsForMovie(dataSource: String, movieId: Double): List<DomainComment> {
        // Ищем узел с нужным фильмом
        val movieSnapshot = databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId)
            .get()
            .await()

        // Проверяем, найден ли фильм с заданным id
        val movieNode = movieSnapshot.children.firstOrNull()

        // Если фильм найден, переходим к узлу comments
        val commentsSnapshot = movieNode
            ?.child(NODE_COMMENTS)
            ?.children
            ?.mapNotNull { it.getValue(DataComment::class.java)?.toDomain() }
            ?: emptyList() // Возвращаем пустой список, если фильм или комментарии не найдены

       return commentsSnapshot
    }

    override suspend fun observeCommentsForMovie(dataSource: String, movieId: Double, onCommentsUpdated: (List<DomainComment>) -> Unit) {
        databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movieSnapshot = snapshot.children.firstOrNull()
                    movieSnapshot?.child(NODE_COMMENTS)?.ref?.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(commentsSnapshot: DataSnapshot) {
                            val comments = commentsSnapshot.children.mapNotNull { it.getValue(DataComment::class.java)?.toDomain() }
                            onCommentsUpdated(comments)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("FirebaseRepositoryImpl", "Error fetching comments: ${error.message}")
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseRepositoryImpl", "Error fetching movie: ${error.message}")
                }
            })
    }

    override suspend fun savingChangeRecord(domainChangelogModel: DomainChangelogModel) {
        val key = databaseReference.child(NODE_LIST_CHANGES).push().key
        key?.let {
            val record = DomainChangelogModel(
                noteId = it,
                username = domainChangelogModel.username,
                noteText = domainChangelogModel.noteText,
                timestamp = domainChangelogModel.timestamp
            )

            databaseReference.child(NODE_LIST_CHANGES).child(it).setValue(record).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun getRecordsOfChanges(): List<DomainChangelogModel> {
        val snapshot = databaseReference.child(NODE_LIST_CHANGES).get().await()
        return snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(DomainChangelogModel::class.java)
        }
            .map {
                DomainChangelogModel(
                    noteId = it.noteId,
                    username = it.username,
                    noteText = it.noteText,
                    timestamp = it.timestamp
                )
            }
    }

    override suspend fun removeRecordsOfChanges(id: String) {
        try {
            val snapshot = databaseReference
                .child(NODE_LIST_CHANGES)
                .orderByKey()
                .equalTo(id)
                .get()
                .await()

            if (snapshot.exists() && snapshot.hasChildren()) {
                // Проходим по всем найденным элементам
                for (filmSnapshot in snapshot.children) {
                    filmSnapshot.ref.removeValue().await() // Удаляем запись
                }
            } else {
                Log.e("RemoveMovie", "No matching record found with id: $id")
            }

        } catch (e: Exception) {
            Log.e("RemoveMovie", "Error: ${e.message}")
        }
    }

    override suspend fun sendingToTheViewedFolder(movieId: Double) {
        try {
            val snapshot = databaseReference
                .child(NODE_LIST_MOVIES)
                .orderByChild("id")
                .equalTo(movieId)
                .get()
                .await()

            if (snapshot.exists()) {
                val movieSnapshot = snapshot.children.firstOrNull() // Берём первую подходящую запись
                val movieKey = movieSnapshot?.key // Получаем ключ записи

                if (movieSnapshot != null && movieKey != null) {
                    val movieData = movieSnapshot.value // Получаем данные записи

                    // Копируем запись в новую папку
                    databaseReference
                        .child(NODE_LIST_WATCHED_MOVIES)
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

}


