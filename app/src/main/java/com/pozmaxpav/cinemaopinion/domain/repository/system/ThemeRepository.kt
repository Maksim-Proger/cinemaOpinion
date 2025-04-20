package com.pozmaxpav.cinemaopinion.domain.repository.system

interface ThemeRepository {
    fun saveModeApplicationTheme(isModeTheme: Boolean)
    fun getModeApplicationTheme(): Boolean
    fun saveModeActivationSystemTheme(isSystemModeTheme: Boolean)
    fun getModeActivationSystemTheme(): Boolean
    fun saveIndexTheme(index: Int)
    fun getIndexTheme(): Int
}