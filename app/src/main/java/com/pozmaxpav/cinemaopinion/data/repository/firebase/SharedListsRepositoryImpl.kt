package com.pozmaxpav.cinemaopinion.data.repository.firebase

import android.util.Log
import com.example.core.domain.DomainUserModel
import com.example.core.utils.CoreDatabaseConstants.NODE_COMMENTS
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_USERS
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_SHARED_LIST_PROFILE
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.data.mappers.commentToData
import com.pozmaxpav.cinemaopinion.data.mappers.commentToDomain
import com.pozmaxpav.cinemaopinion.data.models.firebase.DataComment
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SharedListsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SharedListsRepository {

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

    override suspend fun createSharedList(
        newList: DomainSharedListModel,
        forProfile: DomainMySharedListModel,
        userCreatorId: String,
        invitedUserAddress: List<String>
    ) {
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

    override suspend fun getSharedLists(userId: String): List<DomainSharedListModel> {

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

    override suspend fun removeSharedList(listId: String) {
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

    private suspend fun updateUserData(
        userCreatorId: String,
        invitedUserAddress: List<String>,
        forProfile: DomainMySharedListModel,
    ) {
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

    private suspend fun getNikNamesUsers(
        userCreatorId: String,
        invitedUserAddress: List<String>
    ): String {
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

}