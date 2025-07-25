package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import com.pozmaxpav.cinemaopinion.utilits.NODE_SHARED_LIST
import com.pozmaxpav.cinemaopinion.utilits.NODE_SHARED_LIST_PROFILE
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SharedListsRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : SharedListsRepository {

    override suspend fun createSharedList(
        newList: DomainSharedListModel,
        forProfile: DomainMySharedListModel,
        userCreatorId: String,
        invitedUserAddress: String
    ) {
        try {
            updateUserData(userCreatorId, invitedUserAddress, forProfile)
            val entryWithUsers = newList.copy(users = getNikNamesUsers(userCreatorId, invitedUserAddress))

            val key = databaseReference.child(NODE_SHARED_LIST).push().key
            key?.let {
                databaseReference
                    .child(NODE_SHARED_LIST)
                    .child(it)
                    .setValue(entryWithUsers)
                    .await()
            } ?: throw Exception("Failed to generate key")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun updateUserData(
        userCreatorId: String,
        invitedUserAddress: String,
        forProfile: DomainMySharedListModel,
    ) {
        if (userCreatorId.isEmpty()) throw IllegalArgumentException("User ID cannot be empty")
        if (invitedUserAddress.isEmpty()) throw IllegalArgumentException("User Email cannot be empty")

        // region userCreator
        val userCreator = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userCreatorId)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with ID = $userCreatorId not found.")

        val entryForUserCreator = databaseReference
            .child(NODE_LIST_USERS)
            .child(userCreator)
            .child(NODE_SHARED_LIST_PROFILE)
            .push().key

        databaseReference
            .child(NODE_LIST_USERS)
            .child(userCreator)
            .child(NODE_SHARED_LIST_PROFILE)
            .child(entryForUserCreator!!)
            .setValue(forProfile)
            .await()
        // endregion

        // region invitedUser
        val invitedUser = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("email")
            .equalTo(invitedUserAddress)
            .get()
            .await()
            .children.firstOrNull()?.key
            ?: throw IllegalArgumentException("User with Email = $invitedUserAddress not found.")

        val entryForInvitedUser = databaseReference
            .child(NODE_LIST_USERS)
            .child(invitedUser)
            .child(NODE_SHARED_LIST_PROFILE)
            .push().key

        databaseReference
            .child(NODE_LIST_USERS)
            .child(invitedUser)
            .child(NODE_SHARED_LIST_PROFILE)
            .child(entryForInvitedUser!!)
            .setValue(forProfile)
            .await()
        // endregion

    }

    private suspend fun getNikNamesUsers(
        userCreatorId: String,
        invitedUserAddress: String
    ) : String {

        // Получаем ники без создания полных объектов User
        val creatorNick = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("id")
            .equalTo(userCreatorId)
            .get()
            .await()
            .children
            .firstOrNull()
            ?.getValue(User::class.java)
            ?.nikName
            ?: ""  // или другое значение по умолчанию

        val invitedNick = databaseReference
            .child(NODE_LIST_USERS)
            .orderByChild("email")
            .equalTo(invitedUserAddress)
            .get()
            .await()
            .children
            .firstOrNull()
            ?.getValue(User::class.java)
            ?.nikName
            ?: ""  // или другое значение по умолчанию

        return "$creatorNick, $invitedNick"

    }

}