package com.pozmaxpav.cinemaopinion.domain.repository.system

interface SystemRepositoryApp {
    fun saveUserId(userId: String)
    fun getUserId(): String?
    fun saveAppVersion(version: String)
    fun getAppVersion(): String?
    fun saveResultChecking(resultChecking: Boolean)
    fun getResultChecking(): Boolean
}