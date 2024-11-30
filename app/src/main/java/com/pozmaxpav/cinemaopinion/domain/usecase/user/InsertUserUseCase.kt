package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class InsertUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: DomainUser) {
        userRepository.insertUser(user)
    }
}