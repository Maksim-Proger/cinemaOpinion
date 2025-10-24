package com.example.ui.data.repository

import android.content.Context
import com.example.ui.data.SharedPreferencesUI
import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val context: Context
) : ThemeRepository {
    override fun saveModeApplicationTheme(isModeTheme: Boolean) {
        SharedPreferencesUI.saveModeApplicationTheme(context, isModeTheme)
    }
    override fun getModeApplicationTheme(): Boolean {
        return SharedPreferencesUI.getModeApplicationTheme(context)
    }
    override fun saveModeActivationSystemTheme(isSystemModeTheme: Boolean) {
        SharedPreferencesUI.saveModeActivationSystemTheme(context, isSystemModeTheme)
    }
    override fun getModeActivationSystemTheme(): Boolean {
        return SharedPreferencesUI.getModeActivationSystemTheme(context)
    }
    override fun saveIndexTheme(index: Int) {
        SharedPreferencesUI.saveIndexTheme(context, index)
    }
    override fun getIndexTheme(): Int {
        return SharedPreferencesUI.getIndexTheme(context)
    }
}