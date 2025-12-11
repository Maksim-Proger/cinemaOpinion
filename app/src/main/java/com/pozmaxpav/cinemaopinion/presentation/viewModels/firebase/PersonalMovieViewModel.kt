package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.state.LoadingState
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments.CommentsUseCases
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies.AddMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies.DeleteMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments.GetCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies.GetMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments.ObserveListCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies.ObserveListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.comments.UpdateCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personallist.movies.MoviesUseCases
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
    private val commentsUseCases: CommentsUseCases,
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private val _movieDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val movieDownloadStatus = _movieDownloadStatus.asStateFlow()

    private val _listSelectedMovies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val listSelectedMovies: StateFlow<List<DomainSelectedMovieModel>> = _listSelectedMovies

    private val _commentsDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val commentsDownloadStatus = _commentsDownloadStatus.asStateFlow()

    private val _listComments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val listComments = _listComments.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

    fun addComment(
        userId: String,
        selectedMovieId: Int,
        username: String,
        textComment: String
    ) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    username = username,
                    commentText = textComment,
                    timestamp = System.currentTimeMillis()
                )
                commentsUseCases.addComment(userId, selectedMovieId, comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getComments(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            _commentsDownloadStatus.value = LoadingState.Loading
            try {
                _listComments.value = commentsUseCases.getComments(userId, selectedMovieId)
                _commentsDownloadStatus.value = LoadingState.Success
            } catch (e: Exception) {
                _commentsDownloadStatus.value = LoadingState.Error
                e.printStackTrace()
            }
        }
    }
    fun observeComments(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                commentsUseCases.observeComments(
                    userId,
                    selectedMovieId
                ) { onCommentsUpdated ->
                    _listComments.value = onCommentsUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updateComment(
        userId: String,
        userName: String,
        selectedMovieId: Int,
        commentId: String,
        newCommentText: String
    ) {
        viewModelScope.launch {
            try {
                val selectedComment = DomainCommentModel(
                    commentId,
                    userName,
                    newCommentText,
                    System.currentTimeMillis()
                )
                commentsUseCases.updateComment(userId, selectedMovieId, commentId, selectedComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addMovie(userId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                val movies = moviesUseCases.getMovies(userId)
                if (movies.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    moviesUseCases.addMovie(userId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovies(userId: String) {
        viewModelScope.launch {
            _movieDownloadStatus.value = LoadingState.Loading
            try {
                _listSelectedMovies.value = moviesUseCases.getMovies(userId)
                _movieDownloadStatus.value = LoadingState.Success
            } catch (e: Exception) {
                _movieDownloadStatus.value = LoadingState.Error
                e.printStackTrace()
            }

        }
    }
    fun observeMovies(userId: String) {
        viewModelScope.launch {
            try {
                moviesUseCases.observeList(userId) { onSelectedMoviesUpdated ->
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
                moviesUseCases.dellMovie(userId, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun sendingToNewDirectory(
//        userId: String,
//        dataSource: String,
//        directionDataSource: String,
//        selectedMovieId: Int
//    ) {
//        viewModelScope.launch {
//            try {
//                sendingToNewDirectoryUseCase(userId, dataSource, directionDataSource, selectedMovieId)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    public override fun onCleared() {
        moviesUseCases.observeList.removeListener()
        commentsUseCases.observeComments.removeListener()
        super.onCleared()
    }

}