package com.pozmaxpav.cinemaopinion.data.db.sharedpreferences

import android.content.Context

object SharedPreferences {
    private const val THEME_PREFERENCES = "theme_preferences"
    private const val SYSTEM_PREFERENCES = "system_preferences"
    private const val MODE_APPLICATION_THEME = "mode_application_theme"
    private const val SYSTEM_THEME_ACTIVATION_MODE = "system_theme_activation_mode"
    private const val APP_VERSION_KEY = "app_version_key"
    private const val RESULT_CHECKING_APP_VERSION = "result_checking"
    private const val REGISTRATION_FLAG = "registration_flag"
    private const val USER_ID = "user_id"

    fun saveRegistrationFlag(context: Context, registrationFlag: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(REGISTRATION_FLAG, registrationFlag).apply()
    }
    fun getRegistrationFlag(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(REGISTRATION_FLAG, false)
    }
    fun saveUserId(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(USER_ID, userId).apply()
    }
    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID, null)
    }
    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove(USER_ID)
            .remove(REGISTRATION_FLAG)
            .apply()
    }

    fun saveAppVersion(context: Context, version: String) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(APP_VERSION_KEY, version).apply()
    }
    fun getAppVersion(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APP_VERSION_KEY, null)
    }
    fun saveResultChecking(context: Context, resultChecking: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(RESULT_CHECKING_APP_VERSION, resultChecking).apply()
    }
    fun getResultChecking(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(RESULT_CHECKING_APP_VERSION, false)
    }

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