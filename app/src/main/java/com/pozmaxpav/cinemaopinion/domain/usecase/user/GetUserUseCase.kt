package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): DomainUser? { // метод getUserUseCase возвращает User? (nullable)
        return userRepository.getUser()
    }
}