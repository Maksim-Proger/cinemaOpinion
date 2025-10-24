package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepositoryApp
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val repository: SystemRepositoryApp
) {
    operator fun invoke(): String? {
        return repository.getUserId()
    }
}