package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.repository.firebase.SharedListsRepository
import javax.inject.Inject

class CreatingListUseCase @Inject constructor(
    private val repository: SharedListsRepository
) {
    suspend operator fun invoke(
        newList: DomainSharedListModel,
        forProfile: DomainMySharedListModel,
        userCreatorId: String,
        invitedUserAddress: List<String>
    ) {
        repository.createList(newList, forProfile, userCreatorId, invitedUserAddress)
    }
}