package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepo
import javax.inject.Inject

class UpdateSpecificFieldUseCase @Inject constructor(
    private val repository: UserRepo
) {
    suspend operator fun invoke(userId: String, fieldName: String, newValue: Any) {
        repository.updateSpecificField(userId,fieldName, newValue)
    }
}