package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.example.core.domain.DomainUserModel
import com.example.core.utils.CoreDatabaseConstants
import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : UserRepo {
    override suspend fun getUserData(userId: String): DomainUserModel? {
        if (userId.isEmpty()) return null

        val snapshot =
            databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).child(userId).get()
                .await()
        return snapshot.getValue(DomainUserModel::class.java)?.let { userSnapshot ->
            DomainUserModel(
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

    override suspend fun updatingUserData(domainUserModel: DomainUserModel) {
        val userId = domainUserModel.id
        if (userId.isNotEmpty()) {
            // Сохраняем данные по ID
            databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).child(userId)
                .setValue(domainUserModel).await()
        } else {
            throw Exception("User ID is missing")
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

            databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).child(userId)
                .updateChildren(updates).await()
        } else {
            throw Exception("User ID is missing")
        }
    }

    override suspend fun getUsers(): List<DomainUserModel> {
        val snapshot = databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).get().await()
        return snapshot.children.mapNotNull { childrenSnapshot ->
            childrenSnapshot.getValue(DomainUserModel::class.java)
        }
            .map {
                DomainUserModel(
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
}