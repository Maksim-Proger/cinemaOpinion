package com.pozmaxpav.cinemaopinion.data.repository

import android.content.Context
import com.pozmaxpav.cinemaopinion.data.sharedpreferences.SharedPreferences
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val context: Context
) : ThemeRepository, SystemSharedPreferencesRepository {

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

}