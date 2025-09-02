package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.di.SystemRepositoryQualifier
import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepository
import javax.inject.Inject

class GetRegistrationFlagUseCase @Inject constructor(
    @SystemRepositoryQualifier private val repository: SystemRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.getRegistrationFlag()
    }
}