package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class GetAwardsUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String): List<String> {
        return repository.getAwards(userId)
    }
}