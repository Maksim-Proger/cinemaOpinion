package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.example.core.domain.DomainUserModel
import com.example.core.utils.CoreDatabaseConstants.COMMENTS_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.LISTS_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.MOVIES_KEY_LISTENER
import com.example.core.utils.CoreDatabaseConstants.NODE_COMMENTS
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_CHANGES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST_PROFILE
import com.example.core.utils.FirebaseListenerHolder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.models.firebase.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SharedListsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference,
    private val listenerHolder: FirebaseListenerHolder
) : SharedListsRepository {
    // TODO: Проверить методы!
//    override suspend fun getMovieById(listId: String, movieId: Int): DomainSelectedMovieModel? {
//        if (listId.isEmpty()) throw IllegalArgumentException("List with ID $listId not found")
//
//        val sharedListKey = databaseReference
//            .child(NODE_SHARED_LIST)
//            .orderByChild("listId")
//            .equalTo(listId)
//            .get().await()
//            .children.firstOrNull()?.key
//            ?: throw java.lang.IllegalArgumentException("List with ID $listId not found")
//
//        val movieKey = databaseReference
//            .child(NODE_SHARED_LIST)
//            .child(sharedListKey)
//            .child(NODE_SHARED_LIST_MOVIES)
//            .orderByChild("id")
//            .equalTo(movieId.toDouble())
//            .get().await()
//            .children.firstOrNull()?.key
//            ?: throw java.lang.IllegalArgumentException("Movie with ID $movieId not found")
//
//        val movieData = databaseReference
//            .child(listId)
//            .child(movieKey)
//            .get()
//            .await()
//
//        return movieData.getValue(DomainSelectedMovieModel::class.java)
//    }
//    override suspend fun sendingToNewDirectory(dataSource: String, directionDataSource: String, movieId: Double) {
//        try {
//            val snapshot = databaseReference
//                .child(dataSource)
//                .orderByChild("id")
//                .equalTo(movieId)
//                .get()
//                .await()
//
//            if (snapshot.exists()) {
//                val movieSnapshot =
//                    snapshot.children.firstOrNull() // Берём первую подходящую запись
//                val movieKey = movieSnapshot?.key // Получаем ключ записи
//
//                if (movieSnapshot != null && movieKey != null) {
//                    val movieData = movieSnapshot.value // Получаем данные записи
//
//                    // Копируем запись в новую папку
//                    databaseReference
//                        .child(directionDataSource)
//                        .child(movieKey)
//                        .setValue(movieData)
//                        .await()
//
//                    // Удаляем запись после переноса
//                    databaseReference
//                        .child(dataSource)
//                        .child(movieKey)
//                        .removeValue()
//                        .await()
//
//                    changeRecords(movieId, directionDataSource)
//                }
//            }
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
//                databaseReference
//                    .child(NODE_LIST_CHANGES)
//                    .child(childSnapshot.key!!)
//                    .updateChildren(updates).await()
//            }
//        }
//    }













    override suspend fun addMovie(listId: String, selectedMovie: DomainSelectedMovieModel) {
        if (listId.isEmpty()) throw IllegalArgumentException("List ID cannot be empty")

        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")

        // генерируем новый узел для фильма
        val movieRef = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .push()

        // пишем в него данные
        movieRef.setValue(selectedMovie).await()
    }
    override suspend fun removeMovie(listId: String, movieId: Int) {
        if (listId.isEmpty()) throw IllegalArgumentException("List ID cannot be empty")

        // Находим ключ списка
        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")

        // Переходим в shared_list_movies
        val moviesNode = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .get()
            .await()

        // Ищем фильм по id
        val movieSnapshot = moviesNode.children.firstOrNull { movie ->
            movie.child("id").getValue(Int::class.java) == movieId
        } ?: throw IllegalArgumentException("Movie with ID $movieId not found")

        // Удаляем конкретный фильм
        movieSnapshot.ref.removeValue().await()
    }
    override suspend fun getMovies(listId: String): List<DomainSelectedMovieModel> {
        if (listId.isEmpty()) throw IllegalArgumentException("List with ID $listId not found")

        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")

        return databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .get()
            .await()
            .children.mapNotNull { it.getValue(DomainSelectedMovieModel::class.java) }
    }
    override suspend fun observeListMovies(listId: String, onMoviesUpdated: (List<DomainSelectedMovieModel>) -> Unit) {
        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")

        val moviesRef = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)

        // 3. Создаём слушатель
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val movies = snapshot.children.mapNotNull {
                    it.getValue(DomainSelectedMovieModel::class.java)
                }
                onMoviesUpdated(movies)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Movies listener cancelled: ${error.message}")
            }
        }

        // 4. Навешиваем
        moviesRef.addValueEventListener(listener)

        // 5. Сохраняем с правильной reference
        listenerHolder.addListener(MOVIES_KEY_LISTENER, moviesRef, listener)
    }

    override suspend fun addComment(listId: String, movieId: Int, comment: DomainCommentModel) {
        if (listId.isEmpty()) throw IllegalArgumentException("List with ID $listId not found")

        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get().await()
            .children.firstOrNull()?.key
            ?: throw java.lang.IllegalArgumentException("List with ID $listId not found")

        val movieKey = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get().await()
            .children.firstOrNull()?.key
            ?: throw java.lang.IllegalArgumentException("Movie with ID $movieId not found")

        val commentKey = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .child(movieKey)
            .child(NODE_COMMENTS)
            .push().key
            ?: throw java.lang.IllegalArgumentException("Failed to generate comment Key")

        val dataComment = comment.commentToData().copy(commentId = commentKey)

        databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .child(movieKey)
            .child(NODE_COMMENTS)
            .child(commentKey)
            .setValue(dataComment)
            .await()

    }
    override suspend fun updateComment(listId: String, movieId: Int, commentId: String, selectedComment: DomainCommentModel) {
        if (listId.isEmpty()) throw IllegalArgumentException("List with ID $listId not found")

        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")

        val movieKey = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("Movie with ID $movieId not found")

        val commentsRef = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .child(movieKey)
            .child(NODE_COMMENTS)

        val commentSnapshot = commentsRef
            .orderByChild("commentId")
            .equalTo(commentId)
            .get()
            .await()
            .children.firstOrNull()
            ?: throw IllegalArgumentException("Comment with ID $commentId not found")

        val updatedDataComment = selectedComment.commentToData().copy(commentId = commentId)
        commentSnapshot.ref.setValue(updatedDataComment).await()
    }
    override suspend fun getComments(listId: String, movieId: Int): List<DomainCommentModel> {
        if (listId.isEmpty()) throw IllegalArgumentException("List with ID $listId not found")

        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get().await()
            .children.firstOrNull()?.key
            ?: throw java.lang.IllegalArgumentException("List with ID $listId not found")

        val movieKey = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get().await()
            .children.firstOrNull()?.key
            ?: throw java.lang.IllegalArgumentException("Movie with ID $movieId not found")

        val commentSnapshot = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .child(movieKey)
            .child(NODE_COMMENTS)
            .get().await()

        return commentSnapshot.children.mapNotNull {
            it.getValue(DataComment::class.java)?.commentToDomain()
        }
    }
    override suspend fun observeListComments(listId: String, movieId: Int, onCommentsUpdated: (List<DomainCommentModel>) -> Unit) {
        val sharedListKey = databaseReference
            .child(NODE_SHARED_LIST)
            .orderByChild("listId")
            .equalTo(listId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("List with ID $listId not found")
        val movieKey = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .orderByChild("id")
            .equalTo(movieId.toDouble())
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("Movie with ID $movieId not found")
        val commentsRef = databaseReference
            .child(NODE_SHARED_LIST)
            .child(sharedListKey)
            .child(NODE_SHARED_LIST_MOVIES)
            .child(movieKey)
            .child(NODE_COMMENTS)

        // 4. Создаём listener
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = snapshot.children.mapNotNull {
                    it.getValue(DataComment::class.java)?.commentToDomain()
                }
                onCommentsUpdated(comments)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Comments load cancelled: ${error.message}")
            }
        }

        // 5. Подписываемся на updates
        commentsRef.addValueEventListener(listener)

        // 6. Добавляем в Holder корректную пару ref + listener
        listenerHolder.addListener(COMMENTS_KEY_LISTENER, commentsRef, listener)
    }

    override suspend fun createList(newList: DomainSharedListModel, forProfile: DomainMySharedListModel, userCreatorId: String, invitedUserAddress: List<String>) {
        try {
            updateUserData(userCreatorId, invitedUserAddress, forProfile)
            val entryWithUsers =
                newList.copy(users = getNikNamesUsers(userCreatorId, invitedUserAddress))
            val newRef = databaseReference.child(NODE_SHARED_LIST).push()
            newRef.setValue(entryWithUsers).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override suspend fun removeList(listId: String) {
        try {
            require(listId.isNotEmpty()) { "List ID cannot be empty" }

            val snapshot = databaseReference
                .child(NODE_SHARED_LIST)
                .orderByChild("listId")
                .equalTo(listId)
                .get()
                .await()

            var users = ""
            for (child in snapshot.children) {
                val item = child.getValue(DomainSharedListModel::class.java)
                users = item?.users ?: ""
            }
            updateUserData2(users, listId)

            if (!snapshot.exists()) {
                Log.w("Firebase", "Shared list not found for listId=$listId")
                return
            }

            for (child in snapshot.children) {
                child.ref.removeValue().await()
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Error removing shared list: ${e.message}", e)
        }
    }
    override suspend fun getLists(userId: String): List<DomainSharedListModel> {

        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()
            ?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val sharedListIds = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child("my_shared_list")
            .get()
            .await()
            .children
            .mapNotNull { it.child("listId").getValue(String::class.java) }
            .toSet()

        if (sharedListIds.isEmpty()) return emptyList()

        val sharedLists = databaseReference
            .child(NODE_SHARED_LIST)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(DomainSharedListModel::class.java) }

        return sharedLists.filter { it.listId in sharedListIds }
    }
    override suspend fun getListName(listId: String): String {
        val sharedLists = databaseReference
            .child(NODE_SHARED_LIST)
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(DomainSharedListModel::class.java) }
        for (it in sharedLists) {
            if (it.listId == listId) {
                return it.title
            }
        }
        return ""
    }
    override suspend fun observeLists(userId: String, onListsUpdated: (List<DomainSharedListModel>) -> Unit) {
        if (userId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")

        val userKey = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userId)
            .get()
            .await()
            .children.firstOrNull()
            ?.key
            ?: throw IllegalArgumentException("User with ID $userId not found.")

        val userListsRef = databaseReference
            .child(NODE_LIST_USERS)
            .child(userKey)
            .child("my_shared_list")

        val sharedListsRef = databaseReference
            .child(NODE_SHARED_LIST)

        val listener = object : ValueEventListener {
            override fun onDataChange(sharedSnapshot: DataSnapshot) {
                userListsRef.get().addOnSuccessListener { userSnapshot ->
                    val userListIds = userSnapshot.children
                        .mapNotNull { it.child("listId").getValue(String::class.java) }
                        .toSet()

                    if (userListIds.isEmpty()) {
                        onListsUpdated(emptyList())
                        return@addOnSuccessListener
                    }

                    val lists = sharedSnapshot.children
                        .mapNotNull { it.getValue(DomainSharedListModel::class.java) }
                        .filter { it.listId in userListIds }

                    onListsUpdated(lists)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Shared lists listener cancelled: ${error.message}")
            }
        }

        sharedListsRef.addValueEventListener(listener)
        listenerHolder.addListener(LISTS_KEY_LISTENER, sharedListsRef, listener)
    }

    private suspend fun updateUserData(userCreatorId: String, invitedUserAddress: List<String>, forProfile: DomainMySharedListModel, ) {
        if (userCreatorId.isEmpty()) {
            throw IllegalArgumentException("User ID cannot be empty")
        }

        if (invitedUserAddress.isEmpty()) {
            throw IllegalArgumentException("Invited users list cannot be empty")
        }

        if (invitedUserAddress.any { it.isEmpty() }) {
            throw IllegalArgumentException("One or more invited emails are empty")
        }

        val usersSnapshot = databaseReference
            .child(NODE_LIST_USERS)
            .get()
            .await()

        if (!usersSnapshot.exists()) {
            throw IllegalStateException("Users list is empty in DB")
        }

        val idToUserKey = mutableMapOf<String, String>()
        val emailToUserKey = mutableMapOf<String, String>()

        for (snapshot in usersSnapshot.children) {
            val id = snapshot.child("id").getValue(String::class.java)
            val email = snapshot.child("email").getValue(String::class.java)
            val key = snapshot.key

            if (id != null && key != null) idToUserKey[id] = key
            if (email != null && key != null) emailToUserKey[email] = key
        }

        val creatorUserKey = idToUserKey[userCreatorId]
            ?: throw IllegalArgumentException("User with ID = $userCreatorId not found.")

        val missingEmails = invitedUserAddress.filter { it !in emailToUserKey }

        if (missingEmails.isNotEmpty()) {
            throw IllegalArgumentException("Users not found with emails: $missingEmails")
        }

        val invitedUserKeys = invitedUserAddress.map { emailToUserKey[it]!! }

        suspend fun addProfileToUser(userKey: String) {
            val entryKey = databaseReference
                .child(NODE_LIST_USERS)
                .child(userKey)
                .child(NODE_SHARED_LIST_PROFILE)
                .push().key
                ?: throw IllegalStateException("Failed to generate entry key")

            databaseReference
                .child(NODE_LIST_USERS)
                .child(userKey)
                .child(NODE_SHARED_LIST_PROFILE)
                .child(entryKey)
                .setValue(forProfile)
                .await()
        }

        addProfileToUser(creatorUserKey)

        for (userKey in invitedUserKeys) {
            addProfileToUser(userKey)
        }
    }
    private suspend fun updateUserData2(users: String, listId: String) {
        val usersList = users
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        for (user in usersList) {
            val userKey = databaseReference
                .child(NODE_LIST_USERS)
                .orderByChild("nikName")
                .equalTo(user)
                .get().await().children.firstOrNull()?.key
                ?: throw IllegalArgumentException("User with nikName $user not found")

            val sharedListSnapshot = databaseReference
                .child(NODE_LIST_USERS)
                .child(userKey)
                .child(NODE_SHARED_LIST_PROFILE)
                .orderByChild("listId")
                .equalTo(listId)
                .get().await()

            if (!sharedListSnapshot.exists()) {
                throw IllegalArgumentException("List with listId $listId not found")
            }
            for (childrenSnapshot in sharedListSnapshot.children) {
                childrenSnapshot.ref.removeValue().await()
            }
        }
    }
    private suspend fun getNikNamesUsers(userCreatorId: String, invitedUserAddress: List<String>): String {
        val usersSnapshot = databaseReference
            .child(NODE_LIST_USERS)
            .get()
            .await()

        val allUsers = usersSnapshot.children.mapNotNull {
            it.getValue(DomainUserModel::class.java)
        }

        // Ник создателя
        val creatorNick = allUsers
            .firstOrNull { it.id == userCreatorId }
            ?.nikName
            ?: ""

        // Ники приглашённых
        val invitedNicks = invitedUserAddress
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { email -> allUsers.firstOrNull { it.email == email }?.nikName }

        return listOf(creatorNick)
            .plus(invitedNicks)
            .filter { it.isNotBlank() }
            .joinToString(", ")
    }

    override fun removeMoviesListener() {
        listenerHolder.removeListener(MOVIES_KEY_LISTENER)
    }
    override fun removeCommentsListener() {
        listenerHolder.removeListener(COMMENTS_KEY_LISTENER)
    }
    override fun removeListsListener() {
        listenerHolder.removeListener(LISTS_KEY_LISTENER)
    }

}