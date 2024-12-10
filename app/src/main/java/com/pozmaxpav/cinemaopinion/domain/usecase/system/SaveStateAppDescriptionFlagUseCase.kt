package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import javax.inject.Inject

class SaveStateAppDescriptionFlagUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    operator fun invoke(isAppDescriptionFlag: Boolean) {
        repository.saveStateAppDescriptionFlag(isAppDescriptionFlag)
    }
}