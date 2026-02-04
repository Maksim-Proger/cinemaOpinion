package com.pozmaxpav.cinemaopinion.domain.repository.system

import android.content.Context

interface SystemRepositoryApp {
    fun clearUserData()

    fun saveDeviceRegistrationStatus(status: Boolean)
    fun getDeviceRegistrationStatus(): Boolean

    fun savePushToken(pushToken: String)
    fun getPushToken(): String?

    fun saveUserId(userId: String)
    fun getUserId(): String?

    fun saveRegistrationFlag(registrationFlag: Boolean)
    fun getRegistrationFlag(): Boolean

    fun saveAppVersion(version: String)
    fun getAppVersion(): String?

    fun saveResultChecking(resultChecking: Boolean)
    fun getResultChecking(): Boolean
}