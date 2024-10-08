package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository

class SaveModeApplicationThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(isModeTheme: Boolean) {
        themeRepository.saveModeApplicationTheme(isModeTheme)
    }
}