package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemSharedPreferencesRepository
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    operator fun invoke() : String? {
        return repository.getAppVersion()
    }
}

