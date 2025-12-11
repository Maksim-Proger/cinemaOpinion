package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.lists

import javax.inject.Inject

data class ListsUseCases @Inject constructor(
    val addList: CreatingListUseCase,
    val getLists: GetListsUseCase,
    val getListName: GetListNameUseCase,
    val removeList: RemoveListUseCase,
    val observeLists: ObserveListsUseCase
)