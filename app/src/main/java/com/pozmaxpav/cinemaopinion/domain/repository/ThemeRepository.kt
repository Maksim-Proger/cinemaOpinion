package com.pozmaxpav.cinemaopinion.domain.repository

interface ThemeRepository {
    fun saveModeApplicationTheme(isModeTheme: Boolean)
    fun getModeApplicationTheme(): Boolean
    fun saveModeActivationSystemTheme(isSystemModeTheme: Boolean)
    fun getModeActivationSystemTheme(): Boolean
}