package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.users

import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepository
import javax.inject.Inject

class UpdatingUserDataUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) {
        repository.updatingUserData(user)
    }
}