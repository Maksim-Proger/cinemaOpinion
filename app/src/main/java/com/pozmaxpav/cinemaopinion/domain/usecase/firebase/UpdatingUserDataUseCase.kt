package com.pozmaxpav.cinemaopinion.domain.usecase.firebase

import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.repository.repositoryfirebase.FirebaseRepository
import javax.inject.Inject

class UpdatingUserDataUseCase @Inject constructor(
    private val repository: FirebaseRepository
) {
    suspend operator fun invoke(domainUser: DomainUser) {
        repository.updatingUserData(domainUser)
    }
}