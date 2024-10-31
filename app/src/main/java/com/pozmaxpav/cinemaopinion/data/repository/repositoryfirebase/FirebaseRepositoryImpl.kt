package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.data.firebase.mappers.toData
import com.pozmaxpav.cinemaopinion.data.firebase.mappers.toDomain
import com.pozmaxpav.cinemaopinion.data.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
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

    override suspend fun getMovie(): List<SelectedMovie> {
        val snapshot = databaseReference.child("list_movies").get().await()
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

    override suspend fun addCommentToMovie(
        movieId: Double,
        comment: DomainComment
    ) {
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

    override suspend fun getCommentsForMovie(movieId: Double): List<DomainComment> {
        // Ищем узел с нужным фильмом
        val movieSnapshot = databaseReference
            .child("list_movies")
            .orderByChild("id")
            .equalTo(movieId)
            .get()
            .await()

        // Проверяем, найден ли фильм с заданным id
        val movieNode = movieSnapshot.children.firstOrNull()

        // Если фильм найден, переходим к узлу comments
        val commentsSnapshot = movieNode
            ?.child("comments")
            ?.children
            ?.mapNotNull { it.getValue(DataComment::class.java)?.toDomain() }
            ?: emptyList() // Возвращаем пустой список, если фильм или комментарии не найдены

       return commentsSnapshot
    }
}


