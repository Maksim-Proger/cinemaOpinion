package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.di.ThemeRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import javax.inject.Inject

class SaveModeActivationSystemThemeUseCase @Inject constructor(
    @ThemeRepositoryQualifier private val themeRepository: ThemeRepository
) {
    fun execute(isSystemModeTheme: Boolean) {
        themeRepository.saveModeActivationSystemTheme(isSystemModeTheme)
    }
}