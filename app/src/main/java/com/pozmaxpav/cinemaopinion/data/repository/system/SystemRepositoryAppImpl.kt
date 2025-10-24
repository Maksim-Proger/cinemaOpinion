package com.pozmaxpav.cinemaopinion.data.repository.system

import android.content.Context
import com.pozmaxpav.cinemaopinion.data.repository.SharedPreferencesApp
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepositoryApp
import javax.inject.Inject

class SystemRepositoryAppImpl @Inject constructor(
    private val context: Context
) : SystemRepositoryApp {
    override fun saveUserId(userId: String) {
        SharedPreferencesApp.saveUserId(context, userId)
    }

    override fun getUserId(): String? {
        return SharedPreferencesApp.getUserId(context)
    }

    override fun saveAppVersion(version: String) {
        SharedPreferencesApp.saveAppVersion(context, version)
    }

    override fun getAppVersion(): String? {
        return SharedPreferencesApp.getAppVersion(context)
    }

    override fun saveResultChecking(resultChecking: Boolean) {
        SharedPreferencesApp.saveResultChecking(context, resultChecking)
    }

    override fun getResultChecking(): Boolean {
        return SharedPreferencesApp.getResultChecking(context)
    }
}