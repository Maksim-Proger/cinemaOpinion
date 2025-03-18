package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.repository.remote.FirebaseRepository
import javax.inject.Inject

class UpdateSpecificFieldUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, fieldName: String, newValue: Any) {
        repository.updateSpecificField(userId,fieldName, newValue)
    }
}