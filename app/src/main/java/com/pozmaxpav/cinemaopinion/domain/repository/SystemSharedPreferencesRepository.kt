package com.pozmaxpav.cinemaopinion.domain.repository


interface SystemSharedPreferencesRepository {
    fun saveAppVersion(version: String)
    fun getAppVersion(): String?
    fun saveResultChecking(resultChecking: Boolean)
    fun getResultChecking(): Boolean
}
