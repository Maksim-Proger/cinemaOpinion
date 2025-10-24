package com.pozmaxpav.cinemaopinion.domain.usecase.system

import com.pozmaxpav.cinemaopinion.domain.repository.system.SystemRepositoryApp
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    private val repository: SystemRepositoryApp
) {
    operator fun invoke(userId: String) {
        repository.saveUserId(userId)
    }
}