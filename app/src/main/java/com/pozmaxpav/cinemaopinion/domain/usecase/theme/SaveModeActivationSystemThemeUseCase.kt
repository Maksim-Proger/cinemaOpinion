package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository

class SaveModeActivationSystemThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(isSystemModeTheme: Boolean) {
        themeRepository.saveModeActivationSystemTheme(isSystemModeTheme)
    }
}