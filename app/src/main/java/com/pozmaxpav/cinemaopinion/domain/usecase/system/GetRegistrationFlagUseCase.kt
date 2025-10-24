package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepositoryApp
import javax.inject.Inject

class GetRegistrationFlagUseCase @Inject constructor(
    private val repository: SystemRepositoryApp
) {
    operator fun invoke(): Boolean {
        return repository.getRegistrationFlag()
    }
}