package com.example.ui.domain.usecases

import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject

class SaveModeActivationSystemThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(isSystemModeTheme: Boolean) {
        themeRepository.saveModeActivationSystemTheme(isSystemModeTheme)
    }
}