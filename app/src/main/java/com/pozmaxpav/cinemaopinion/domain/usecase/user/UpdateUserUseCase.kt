package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: DomainUser) {
        userRepository.updateUser(user)
    }
}