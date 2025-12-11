package com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments


import javax.inject.Inject

data class CommentsUseCases @Inject constructor(
    val addComment: AddCommentUseCase,
    val getComments: GetCommentsUseCase,
    val updateComment: UpdateCommentUseCase,
    val observeComments: ObserveListCommentsUseCase
)
