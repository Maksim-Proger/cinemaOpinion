package com.example.auth.data.repository.system

import android.content.Context
import com.example.auth.data.SharedPreferencesAuth
import com.example.auth.domain.repository.system.SystemRepositoryAuth
import javax.inject.Inject

class SystemRepositoryAuthImpl @Inject constructor(
    private val context: Context
) : SystemRepositoryAuth {
    override fun saveRegistrationFlag(registrationFlag: Boolean) {
        SharedPreferencesAuth.saveRegistrationFlag(context, registrationFlag)
    }
    override fun getRegistrationFlag(): Boolean {
        return SharedPreferencesAuth.getRegistrationFlag(context)
    }
    override fun saveUserId(userId: String) {
        SharedPreferencesAuth.saveUserId(context, userId)
    }
    override fun getUserId(): String? {
        return SharedPreferencesAuth.getUserId(context)
    }
}