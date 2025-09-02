package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepository
import javax.inject.Inject

class ClearUserDataUseCase @Inject constructor(
    @SystemRepositoryQualifier private val repository: SystemRepository
) {
    operator fun invoke() {
        repository.clearUserData()
    }
}