package com.example.auth.data.repository.firebase

import com.example.core.domain.DomainUserModel
import com.example.auth.domain.repository.AuthRepository
import com.example.core.utils.CoreDatabaseConstants
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImplRepository @Inject constructor(
    private val databaseReference: DatabaseReference
) : AuthRepository {

    override suspend fun addUser(domainUserModel: DomainUserModel) {
        val userId = domainUserModel.id
        if (userId.isNotEmpty())
            databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).child(userId).setValue(domainUserModel).await()
        else throw Exception("User ID is missing")
    }

    override suspend fun authorization(email: String, password: String): DomainUserModel? {
        val query = databaseReference.child(CoreDatabaseConstants.NODE_LIST_USERS).orderByChild("email").equalTo(email)
        val userSnapshot = query.get().await()

        if (!userSnapshot.exists()) {
            return null
        }

        for (snapshot in userSnapshot.children) {
            val domainUserModel = snapshot.getValue(DomainUserModel::class.java)
            if (domainUserModel != null) {
                // Проверяем совпадение пароля
                return if (domainUserModel.password == password) {
                    domainUserModel // Возвращаем пользователя, если пароль совпал
                } else {
                    null // Пароль не совпал
                }
            }
        }

        return null
    }

}