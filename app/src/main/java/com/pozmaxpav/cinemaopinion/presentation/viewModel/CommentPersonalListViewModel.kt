package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.CommentPersonalListModel
import com.pozmaxpav.cinemaopinion.domain.usecase.commentPersonalList.CPLGetComments
import com.pozmaxpav.cinemaopinion.domain.usecase.commentPersonalList.CPLInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentPersonalListViewModel @Inject constructor(
    private val insert: CPLInsertUseCase,
    private val getComments: CPLGetComments
) : ViewModel() {
    private val _comments = MutableStateFlow<List<CommentPersonalListModel>>(emptyList())
    val comments: StateFlow<List<CommentPersonalListModel>> = _comments

    fun insertComment(
        movieId: Double,
        comment: String
    ) {
        val resultComment = CommentPersonalListModel(
            id = 0,
            movieId = movieId,
            commentText = comment,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            try {
                insert.invoke(resultComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCommentsList() {
        viewModelScope.launch {
            try {
                getComments.invoke().collect { comments ->
                    _comments.value = comments
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}