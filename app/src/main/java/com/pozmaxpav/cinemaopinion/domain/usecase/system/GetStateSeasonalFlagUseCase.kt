package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import javax.inject.Inject

class GetStateSeasonalFlagUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val  repository: SystemSharedPreferencesRepository
) {
    fun invoke(): Boolean {
        return repository.getStateSeasonalFlag()
    }
}