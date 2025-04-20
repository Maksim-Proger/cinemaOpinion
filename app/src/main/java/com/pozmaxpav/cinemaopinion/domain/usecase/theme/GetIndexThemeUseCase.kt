package com.pozmaxpav.cinemaopinion.domain.usecase.theme

import com.pozmaxpav.cinemaopinion.di.ThemeRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.ThemeRepository
import javax.inject.Inject

class GetIndexThemeUseCase @Inject constructor(
    @ThemeRepositoryQualifier private val repository: ThemeRepository
) {
    operator fun invoke(): Int {
        return repository.getIndexTheme()
    }
}