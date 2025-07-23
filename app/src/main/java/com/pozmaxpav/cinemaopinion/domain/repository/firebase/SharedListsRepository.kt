package com.pozmaxpav.cinemaopinion.domain.repository.firebase

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel

interface SharedListsRepository {
    suspend fun createSharedList(
        newList: DomainSharedListModel,
        forProfile: DomainMySharedListModel,
        userCreatorId: String,
        invitedUserAddress: String
    )
}