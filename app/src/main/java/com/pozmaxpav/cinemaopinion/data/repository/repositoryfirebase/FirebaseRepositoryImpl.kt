package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.data.api.models.modelfirebase.FilmData
import com.pozmaxpav.cinemaopinion.data.mappers.toData
import com.pozmaxpav.cinemaopinion.data.mappers.toDomain
import com.pozmaxpav.cinemaopinion.domain.models.modelfirebase.FilmDomain
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : FirebaseRepository {
    override suspend fun saveMovie(title: String) {
        val filmData = FilmDomain(titleFilm = title).toData() // Преобразуем FilmDomain в FilmData
        val key = databaseReference.child("list_movies").push().key
        key?.let {
            databaseReference.child("list_movies").child(it).setValue(filmData).await()
        } ?: throw Exception("Failed to generate key")
    }

    override suspend fun removeMovie(title: String) { // TODO: Надо проверить работает ли.
        val snapshot = databaseReference.child("list_movies").orderByChild("titleFilm").equalTo(title).get().await()
        for (filmSnapshot in snapshot.children) {
            filmSnapshot.ref.removeValue().await()
        }
    }

    override suspend fun getMovie(): List<FilmDomain> {
        val snapshot = databaseReference.child("list_movies").get().await()
        return snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(FilmData::class.java)
        }
            .map { it.toDomain() } // Преобразуем FilmData в FilmDomain
    }

}