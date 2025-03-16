package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemSharedPreferencesRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.SystemSharedPreferencesRepository
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    @SystemSharedPreferencesRepositoryQualifier private val repository: SystemSharedPreferencesRepository
) {
    operator fun invoke(userId: String) {
        repository.saveUserId(userId)
    }
}