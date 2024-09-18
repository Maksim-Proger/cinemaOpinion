package com.pozmaxpav.cinemaopinion.data.repository

import android.content.Context
import com.pozmaxpav.cinemaopinion.data.sharedpreferences.SharedPreferences
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import javax.inject.Inject

class SharedPreferencesRepository @Inject constructor(
    private val context: Context
) : ThemeRepository {
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
}