package com.pozmaxpav.cinemaopinion.data.sharedpreferences

import android.content.Context

object SharedPreferences {
    private const val THEME_PREFERENCES = "theme_preferences"
    private const val MODE_APPLICATION_THEME = "mode_application_theme"
    private const val SYSTEM_THEME_ACTIVATION_MODE = "system_theme_activation_mode"

    fun saveModeApplicationTheme(context: Context, isModeTheme: Boolean) {
        val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(MODE_APPLICATION_THEME, isModeTheme).apply()
    }

    fun getModeApplicationTheme(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(MODE_APPLICATION_THEME, false)
    }

    fun saveModeActivationSystemTheme(context: Context, isSystemModeTheme: Boolean) {
        val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(SYSTEM_THEME_ACTIVATION_MODE, isSystemModeTheme).apply()
    }

    fun getModeActivationSystemTheme(context: Context) : Boolean {
        val sharedPreferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(SYSTEM_THEME_ACTIVATION_MODE, false)
    }
}