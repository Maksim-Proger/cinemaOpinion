package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    operator fun invoke(): String? {
        return repository.getUserId()
    }
}