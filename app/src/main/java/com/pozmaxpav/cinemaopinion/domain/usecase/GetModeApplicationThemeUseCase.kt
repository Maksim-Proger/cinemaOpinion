package com.pozmaxpav.cinemaopinion.domain.usecase

import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository

class GetModeApplicationThemeUseCase(private val themeRepository: ThemeRepository) {
    fun execute(): Boolean {
        return themeRepository.getModeApplicationTheme()
    }
}