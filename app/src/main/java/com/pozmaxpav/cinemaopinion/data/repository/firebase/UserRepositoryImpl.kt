package com.pozmaxpav.cinemaopinion.data.repository.firebase

import com.google.firebase.database.DatabaseReference
import com.pozmaxpav.cinemaopinion.domain.models.firebase.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import com.pozmaxpav.cinemaopinion.utilits.NODE_LIST_USERS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val databaseReference: DatabaseReference
) : UserRepository {

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
        if (userId.isNotEmpty())
            databaseReference.child(NODE_LIST_USERS).child(userId).setValue(user).await()
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

}