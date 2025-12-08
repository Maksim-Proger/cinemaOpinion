package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.state.LoadingState
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.ObserveListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.SendingToNewDirectoryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.ObserveListCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.UpdateCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val observeListMoviesUseCase: ObserveListMoviesUseCase,

    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val observeListCommentsUseCase: ObserveListCommentsUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,

    private val sendingToNewDirectoryUseCase: SendingToNewDirectoryUseCase
) : ViewModel() {

//    private val _successfulResult = MutableSharedFlow<Pair<String, DomainSelectedMovieModel>>(
//        replay = 0,
//        extraBufferCapacity = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )
//    val successfulResult = _successfulResult.asSharedFlow()
//
//    fun saveMovie(dataSource: String, selectedMovie: DomainSelectedMovieModel) {
//        viewModelScope.launch {
//            try {
//                val movies = getMovieUseCase(dataSource)
//                if (movies.any { it.id == selectedMovie.id }) {
//                    _toastMessage.emit(R.string.movie_has_already_been_added)
//                } else {
//                    saveMovieUseCase(dataSource, selectedMovie)
//                    _toastMessage.emit(R.string.movie_has_been_added_to_general_list)
//                    _successfulResult.emit(dataSource to selectedMovie)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//    fun observeListMovies(dataSource: String) {
//        viewModelScope.launch {
//            observeListMoviesUseCase(dataSource) { updateListMovies ->
//                _movies.value = updateListMovies
//            }
//        }
//    }
//
//    fun getComments(dataSource: String, movieId: Int) {
//        viewModelScope.launch {
//            _commentsDownloadStatus.value = LoadingState.Loading
//            try {
//                val commentList = getCommentsUseCase(dataSource, movieId)
//                _comments.value = commentList
//                delay(500)
//                _commentsDownloadStatus.value = LoadingState.Success
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//    fun observeComments(dataSource: String, movieId: Int) {
//        viewModelScope.launch {
//            observeListCommentsUseCase(dataSource, movieId) { updatedComments ->
//                _comments.value = updatedComments
//            }
//        }
//    }
//    fun updateComment(dataSource: String, userName: String, selectedMovieId: Int, commentId: String, newCommentText: String) {
//        viewModelScope.launch {
//            try {
//                val selectedComment = DomainCommentModel(
//                    commentId,
//                    userName,
//                    newCommentText,
//                    System.currentTimeMillis()
//                )
//
//                updateCommentUseCase(dataSource, selectedMovieId, commentId, selectedComment)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//    fun sendingToNewDirectory(
//        dataSource: String,
//        directionDataSource: String,
//        movieId: Double
//    ) {
//        viewModelScope.launch {
//            try {
//                sendingToNewDirectoryUseCase(dataSource, directionDataSource, movieId)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    public override fun onCleared() {
//        observeListMoviesUseCase.removeListener()
//        observeListCommentsUseCase.removeListener()
//        super.onCleared()
//    }
}

