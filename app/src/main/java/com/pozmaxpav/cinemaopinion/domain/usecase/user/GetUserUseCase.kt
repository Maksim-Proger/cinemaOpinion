package com.pozmaxpav.cinemaopinion.domain.usecase.user

import com.pozmaxpav.cinemaopinion.domain.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? { // метод getUserUseCase возвращает User? (nullable). TODO: почему suspend operator fun???
        return userRepository.getUser()
    }
}