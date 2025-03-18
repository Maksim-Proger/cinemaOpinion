package com.pozmaxpav.cinemaopinion.domain.repository.system

interface SystemSharedPreferencesRepository {
    fun saveAppVersion(version: String)
    fun getAppVersion(): String?
    fun saveResultChecking(resultChecking: Boolean)
    fun getResultChecking(): Boolean

    fun saveRegistrationFlag(registrationFlag: Boolean)
    fun getRegistrationFlag(): Boolean
    fun saveUserId(userId: String)
    fun getUserId(): String?
}
