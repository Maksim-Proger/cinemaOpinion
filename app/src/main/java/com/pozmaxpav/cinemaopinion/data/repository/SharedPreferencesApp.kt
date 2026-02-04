package com.pozmaxpav.cinemaopinion.data.repository

import android.content.Context
import androidx.core.content.edit

object SharedPreferencesApp {
    private const val SYSTEM_PREFERENCES_APP = "system_preferences_app"
    private const val APP_VERSION = "app_version"
    private const val RESULT_CHECKING_APP_VERSION = "result_checking"
    private const val USER_ID = "user_id"
    private const val REGISTRATION_FLAG = "registration_flag"
    private const val PUSH_TOKEN = "push_token"
    private const val STATUS_DEVICE_REGISTERED = "status_device_registered"

    fun saveDeviceRegistrationStatus(context: Context, status: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(STATUS_DEVICE_REGISTERED, status)
        }
    }
    fun getDeviceRegistrationStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(STATUS_DEVICE_REGISTERED, false)
    }

    fun savePushToken(context: Context, pushToken: String) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(PUSH_TOKEN, pushToken)
        }
    }
    fun getPushToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PUSH_TOKEN, null)
    }

    fun saveRegistrationFlag(context: Context, registrationFlag: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(REGISTRATION_FLAG, registrationFlag)
        }
    }
    fun getRegistrationFlag(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(REGISTRATION_FLAG, false)
    }

    fun saveUserId(context: Context, userId: String) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(USER_ID, userId)
        }
    }
    fun getUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID, null)
    }

    fun saveAppVersion(context: Context, version: String) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(APP_VERSION, version)
        }
    }
    fun getAppVersion(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getString(APP_VERSION, null)

    }

    fun saveResultChecking(context: Context, resultChecking: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(RESULT_CHECKING_APP_VERSION, resultChecking)
        }
    }
    fun getResultChecking(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(RESULT_CHECKING_APP_VERSION, false)
    }

    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SYSTEM_PREFERENCES_APP, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(USER_ID)
            remove(REGISTRATION_FLAG)
        }
    }
}