package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class UpdateSeasonalEventPointsUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, fieldName: String, newValue: Any) {
        repository.updateSeasonalEventPoints(userId,fieldName, newValue)
    }
}