package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemSharedPreferencesRepository
import javax.inject.Inject

class SaveRegistrationFlagUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    suspend operator fun invoke(registrationFlag: Boolean) {
        repository.saveRegistrationFlag(registrationFlag)
    }
}