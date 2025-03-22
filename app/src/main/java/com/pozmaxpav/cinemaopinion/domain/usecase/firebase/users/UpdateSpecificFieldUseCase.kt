package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import javax.inject.Inject

class UpdateSpecificFieldUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, fieldName: String, newValue: Any) {
        repository.updateSpecificField(userId,fieldName, newValue)
    }
}