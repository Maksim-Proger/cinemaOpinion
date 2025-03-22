package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.remote.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.MovieRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : MovieRepository {

    override suspend fun saveMovie(dataSource: String, selectedMovie: SelectedMovieModel) {
        val key = databaseReference.child(dataSource).push().key
        key?.let {
            databaseReference.child(dataSource).child(it).setValue(selectedMovie).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun removeMovie(dataSource: String, id: Double) {
        try {
            val snapshot =
                databaseReference
                    .child(dataSource)
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
            // TODO: Добавить отлов ошибки
        }
    }

    override suspend fun getMovie(dataSource: String): List<SelectedMovieModel> {
        val snapshot = databaseReference.child(dataSource).get().await()
        return snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(SelectedMovieModel::class.java)
        }
            .map {
                SelectedMovieModel(
                    id = it.id,
                    nameFilm = it.nameFilm,
                    posterUrl = it.posterUrl
                )
            }
    }

    override suspend fun observeListMovies(
        dataSource: String,
        onMoviesUpdated: (List<SelectedMovieModel>) -> Unit
    ) {
        databaseReference
            .child(dataSource)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movies = snapshot.children.mapNotNull { movieSnapshot ->
                        movieSnapshot.getValue(SelectedMovieModel::class.java)
                    }
                    onMoviesUpdated(movies)
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO: Добавить отлов ошибки
                }
            })
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
        movieId: Double
    ): List<DomainCommentModel> {
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
            ?.mapNotNull { it.getValue(DataComment::class.java)?.commentToDomain() }
            ?: emptyList() // Возвращаем пустой список, если фильм или комментарии не найдены

        return commentsSnapshot
    }

    override suspend fun observeCommentsForMovie(
        dataSource: String,
        movieId: Double,
        onCommentsUpdated: (List<DomainCommentModel>) -> Unit
    ) {
        databaseReference
            .child(dataSource)
            .orderByChild("id")
            .equalTo(movieId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val movieSnapshot = snapshot.children.firstOrNull()
                    movieSnapshot?.child(NODE_COMMENTS)?.ref?.addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(commentsSnapshot: DataSnapshot) {
                            val comments = commentsSnapshot.children.mapNotNull {
                                it.getValue(
                                    DataComment::class.java
                                )?.commentToDomain()
                            }
                            onCommentsUpdated(comments)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // TODO: Добавить отлов ошибки
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO: Добавить отлов ошибки
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

}


