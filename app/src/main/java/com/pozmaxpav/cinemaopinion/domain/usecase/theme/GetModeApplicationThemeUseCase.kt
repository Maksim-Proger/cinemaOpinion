package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.di.ThemeRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.ThemeRepository
import javax.inject.Inject

class GetModeApplicationThemeUseCase @Inject constructor(
    @ThemeRepositoryQualifier private val themeRepository: ThemeRepository
) {
    operator fun invoke(): Boolean {
        return themeRepository.getModeApplicationTheme()
    }
}