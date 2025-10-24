package com.pozmaxpav.cinemaopinion.domain.repository.system

interface SystemRepositoryApp {
    fun clearUserData()

    fun saveUserId(userId: String)
    fun getUserId(): String?

    fun saveRegistrationFlag(registrationFlag: Boolean)
    fun getRegistrationFlag(): Boolean

    fun saveAppVersion(version: String)
    fun getAppVersion(): String?

    fun saveResultChecking(resultChecking: Boolean)
    fun getResultChecking(): Boolean
}