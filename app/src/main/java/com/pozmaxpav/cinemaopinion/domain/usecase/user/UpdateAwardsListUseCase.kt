package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class UpdateAwardsListUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String, newAwards: String) {
        repository.updateAwardsList(userId, newAwards)
    }
}