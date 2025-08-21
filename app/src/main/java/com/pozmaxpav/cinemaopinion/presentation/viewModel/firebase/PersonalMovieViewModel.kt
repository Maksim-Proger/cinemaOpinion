package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddCommentToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddMovieToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.DeleteMovieFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetCommentsFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetListPersonalMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveCommentsFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveListSelectedMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.SendingToNewDirectoryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.UpdateCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalMovieViewModel @Inject constructor(
    private val addMovieToPersonalListUseCase: AddMovieToPersonalListUseCase,
    private val getListPersonalMoviesUseCase: GetListPersonalMoviesUseCase,
    private val observeListSelectedMoviesUseCase: ObserveListSelectedMoviesUseCase,
    private val deleteMovieFromPersonalListUseCase: DeleteMovieFromPersonalListUseCase,
    private val addCommentToPersonalListUseCase: AddCommentToPersonalListUseCase,
    private val getCommentsFromPersonalListUseCase: GetCommentsFromPersonalListUseCase,
    private val observeCommentsForMovieFromPersonalListUseCase: ObserveCommentsFromPersonalListUseCase,
    private val sendingToNewDirectoryUseCase: SendingToNewDirectoryUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase
    ) : ViewModel() {

    private val _listSelectedMovies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val listSelectedMovies: StateFlow<List<DomainSelectedMovieModel>> = _listSelectedMovies

    private val _listComments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val listComments = _listComments.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

    fun addCommentToPersonalList(userId: String, selectedMovieId: Int, username: String, textComment: String) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    username = username,
                    commentText = textComment,
                    timestamp = System.currentTimeMillis()
                )
                addCommentToPersonalListUseCase(userId,selectedMovieId,comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCommentsFromPersonalList(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                val comments = getCommentsFromPersonalListUseCase(userId, selectedMovieId)
                _listComments.value = comments
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun observeCommentsForMovieFromPersonalList(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                observeCommentsForMovieFromPersonalListUseCase(userId, selectedMovieId) { onCommentsUpdated ->
                    _listComments.value = onCommentsUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getListPersonalMovies(userId: String) {
        viewModelScope.launch {
            try {
                val movies = getListPersonalMoviesUseCase(userId)
                _listSelectedMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun addMovieToPersonalList(userId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                val movies = getListPersonalMoviesUseCase(userId)
                if (movies.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    addMovieToPersonalListUseCase(userId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun observeListSelectedMovies(userId: String) {
        viewModelScope.launch {
            try {
                observeListSelectedMoviesUseCase(userId) { onSelectedMoviesUpdated ->
                    _listSelectedMovies.value = onSelectedMoviesUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateComment(userId: String, userName: String, selectedMovieId: Int, commentId: String, newCommentText: String) {
        viewModelScope.launch {
            try {
                val selectedComment = DomainCommentModel(
                    commentId,
                    userName,
                    newCommentText,
                    System.currentTimeMillis()
                )
                updateCommentUseCase(userId, selectedMovieId, commentId, selectedComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSelectedMovie(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                deleteMovieFromPersonalListUseCase(userId, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendingToNewDirectory(userId: String, dataSource: String, directionDataSource: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                sendingToNewDirectoryUseCase(userId, dataSource, directionDataSource, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public override fun onCleared() {
        observeListSelectedMoviesUseCase.removeListener()
        observeCommentsForMovieFromPersonalListUseCase.removeListener()
        super.onCleared()
    }

}