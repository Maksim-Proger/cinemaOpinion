package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user

import com.example.core.domain.DomainUserModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.UserRepo
import javax.inject.Inject

class UpdatingUserDataUseCase @Inject constructor(
    private val repository: UserRepo
) {
    suspend operator fun invoke(domainUserModel: DomainUserModel) {
        repository.updatingUserData(domainUserModel)
    }
}