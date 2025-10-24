package com.example.ui.domain.usecases

import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject

class SaveModeApplicationThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(isModeTheme: Boolean) {
        themeRepository.saveModeApplicationTheme(isModeTheme)
    }
}