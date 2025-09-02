package com.pozmaxpav.cinemaopinion.data.repository.system

import android.content.Context
import com.pozmaxpav.cinemaopinion.data.db.sharedpreferences.SharedPreferences
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepository
import com.pozmaxpav.cinemaopinion.domain.repository.system.ThemeRepository
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val context: Context
) : ThemeRepository, SystemRepository {

    override fun saveModeApplicationTheme(isModeTheme: Boolean) {
        SharedPreferences.saveModeApplicationTheme(context, isModeTheme)
    }
    override fun getModeApplicationTheme(): Boolean {
        return SharedPreferences.getModeApplicationTheme(context)
    }
    override fun saveModeActivationSystemTheme(isSystemModeTheme: Boolean) {
        SharedPreferences.saveModeActivationSystemTheme(context, isSystemModeTheme)
    }
    override fun getModeActivationSystemTheme(): Boolean {
        return SharedPreferences.getModeActivationSystemTheme(context)
    }
    override fun saveIndexTheme(index: Int) {
        SharedPreferences.saveIndexTheme(context, index)
    }
    override fun getIndexTheme(): Int {
        return SharedPreferences.getIndexTheme(context)
    }

    override fun saveAppVersion(version: String) {
        SharedPreferences.saveAppVersion(context, version)
    }
    override fun getAppVersion(): String? {
        return SharedPreferences.getAppVersion(context)
    }
    override fun saveResultChecking(resultChecking: Boolean) {
        SharedPreferences.saveResultChecking(context, resultChecking)
    }
    override fun getResultChecking(): Boolean {
        return SharedPreferences.getResultChecking(context)
    }

    override fun saveRegistrationFlag(registrationFlag: Boolean) {
        SharedPreferences.saveRegistrationFlag(context, registrationFlag)
    }
    override fun getRegistrationFlag(): Boolean {
        return SharedPreferences.getRegistrationFlag(context)
    }
    override fun saveUserId(userId: String) {
        SharedPreferences.saveUserId(context, userId)
    }
    override fun getUserId(): String? {
        return SharedPreferences.getUserId(context)
    }
    override fun clearUserData() {
        SharedPreferences.clearUserData(context)
    }

}