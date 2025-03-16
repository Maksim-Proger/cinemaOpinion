package com.pozmaxpav.cinemaopinion.data.repository.repositoryfirebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.remote.firebase.models.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_COMMENTS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_CHANGES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_MOVIES
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : FirebaseRepository {

    override suspend fun getUsers(): List<User> {
        val snapshot = databaseReference.child(NODE_LIST_USERS).get().await()
        return snapshot.children.mapNotNull { childrenSnapshot ->
            childrenSnapshot.getValue(User::class.java)
        }
            .map {
                User(
                    id = it.id,
                    nikName = it.nikName,
                    email = it.email,
                    password = it.password,
                    awards = it.awards,
                    professionalPoints = it.professionalPoints,
                    seasonalEventPoints = it.seasonalEventPoints
                )
            }
    }

    override suspend fun addUser(user: User) {
        val userId = user.id
        if (userId.isNotEmpty()) databaseReference.child(NODE_LIST_USERS).child(userId)
            .setValue(user).await()
        else throw Exception("User ID is missing")
    }

    override suspend fun updatingUserData(user: User) {
        val userId = user.id
        if (userId.isNotEmpty()) {
            // Сохраняем данные по ID
            databaseReference.child(NODE_LIST_USERS).child(userId).setValue(user).await()
        } else {
            throw Exception("User ID is missing")
        }
    }

    override suspend fun checkLoginAndPassword(email: String, password: String): User? {
        val query = databaseReference.child(NODE_LIST_USERS).orderByChild("email").equalTo(email)
        val userSnapshot = query.get().await()

        if (!userSnapshot.exists()) {
            return null
        }

        for (snapshot in userSnapshot.children) {
            val user = snapshot.getValue(User::class.java)
            if (user != null) {
                // Проверяем совпадение пароля
                if (user.password == password) {
                    return user // Возвращаем пользователя, если пароль совпал
                } else {
                    return null // Пароль не совпал
                }
            }
        }

        return null
    }

    override suspend fun getUserData(userId: String): User? {
        if (userId.isEmpty()) {
            return null
        }

        val snapshot = databaseReference.child(NODE_LIST_USERS).child(userId).get().await()
        return snapshot.getValue(User::class.java)?.let { userSnapshot ->
            User(
                id = userSnapshot.id,
                nikName = userSnapshot.nikName,
                email = userSnapshot.email,
                password = userSnapshot.password,
                awards = userSnapshot.awards,
                professionalPoints = userSnapshot.professionalPoints,
                seasonalEventPoints = userSnapshot.seasonalEventPoints
            )
        }
    }

    override suspend fun updateSpecificField(
        userId: String,
        fieldName: String,
        newValue: Any
    ) {
        if (userId.isNotEmpty() && fieldName.isNotEmpty()) {
            val updates = mapOf(
                fieldName to newValue
            )

            databaseReference.child(NODE_LIST_USERS).child(userId).updateChildren(updates).await()
        } else {
            throw Exception("User ID is missing")
        }
    }

    override suspend fun saveMovie(dataSource: String, selectedMovie: SelectedMovie) {
//        val filmData = SelectedMovie( // TODO: Надо разобрать зачем мне тут снова создавать модель?
//            selectedMovie.id,
//            selectedMovie.nameFilm,
//            selectedMovie.posterUrl,
//        )
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

    override suspend fun observeListMovies(
        dataSource: String,
        onMoviesUpdated: (List<SelectedMovie>) -> Unit
    ) {
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
                    // TODO: Добавить отлов ошибки
                }
            })
    }

    override suspend fun addCommentToMovie(
        dataSource: String,
        movieId: Double,
        comment: DomainComment
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
    ): List<DomainComment> {
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
        onCommentsUpdated: (List<DomainComment>) -> Unit
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
                // TODO: Добавить отлов ошибки
            }

        } catch (e: Exception) {
            // TODO: Добавить отлов ошибки
        }
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


