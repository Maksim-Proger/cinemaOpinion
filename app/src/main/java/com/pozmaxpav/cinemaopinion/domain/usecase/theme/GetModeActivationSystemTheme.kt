package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.di.ThemeRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.ThemeRepository
import javax.inject.Inject

class GetModeActivationSystemTheme @Inject constructor(
    @ThemeRepositoryQualifier private val themeRepository: ThemeRepository
) {
    fun execute(): Boolean {
        return themeRepository.getModeActivationSystemTheme()
    }
}