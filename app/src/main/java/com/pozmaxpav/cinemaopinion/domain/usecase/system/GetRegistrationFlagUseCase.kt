package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import javax.inject.Inject

class GetRegistrationFlagUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.getRegistrationFlag()
    }
}