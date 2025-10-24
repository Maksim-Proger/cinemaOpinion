package com.example.ui.domain.usecases

import com.example.ui.domain.repositories.ThemeRepository
import javax.inject.Inject

class GetModeActivationSystemThemeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Boolean {
        return themeRepository.getModeActivationSystemTheme()
    }
}