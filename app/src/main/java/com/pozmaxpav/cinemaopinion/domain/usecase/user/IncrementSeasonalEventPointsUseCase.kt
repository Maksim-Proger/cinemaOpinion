package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class IncrementSeasonalEventPointsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, increment: Long) {
        repository.incrementSeasonalEventPoints(userId, increment)
    }
}