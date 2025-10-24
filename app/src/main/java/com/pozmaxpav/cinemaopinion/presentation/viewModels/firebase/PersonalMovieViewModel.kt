package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.DeleteMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveListCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveListMoviesUseCase
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
    private val addMovieUseCase: AddMovieUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val observeListMoviesUseCase: ObserveListMoviesUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val observeCommentsForMovieFromPersonalListUseCase: ObserveListCommentsUseCase,
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

    fun addComment(userId: String, selectedMovieId: Int, username: String, textComment: String) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    username = username,
                    commentText = textComment,
                    timestamp = System.currentTimeMillis()
                )
                addCommentUseCase(userId,selectedMovieId,comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getComments(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                _listComments.value = getCommentsUseCase(userId, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeComments(userId: String, selectedMovieId: Int) {
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

    fun addMovie(userId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                val movies = getMoviesUseCase(userId)
                if (movies.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    addMovieUseCase(userId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovies(userId: String) {
        viewModelScope.launch {
            try {
                val movies = getMoviesUseCase(userId)
                _listSelectedMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    fun observeMovies(userId: String) {
        viewModelScope.launch {
            try {
                observeListMoviesUseCase(userId) { onSelectedMoviesUpdated ->
                    _listSelectedMovies.value = onSelectedMoviesUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deleteMovie(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                deleteMovieUseCase(userId, selectedMovieId)
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
        observeListMoviesUseCase.removeListener()
        observeCommentsForMovieFromPersonalListUseCase.removeListener()
        super.onCleared()
    }

}