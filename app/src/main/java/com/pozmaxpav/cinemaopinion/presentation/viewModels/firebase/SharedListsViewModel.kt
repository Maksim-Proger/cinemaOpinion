package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.state.LoadingState
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.comments.CommentsUseCases
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.lists.ListsUseCases
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlist.movies.MoviesUseCases
import com.pozmaxpav.cinemaopinion.utilits.formatTextWithUnderscores
import com.pozmaxpav.cinemaopinion.utilits.simpleTransliterate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SharedListsViewModel @Inject constructor(
    private val listsUseCases: ListsUseCases,
    private val moviesUseCases: MoviesUseCases,
    private val commentsUseCases: CommentsUseCases
) : ViewModel() {

    private val _movieDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val movieDownloadStatus = _movieDownloadStatus.asStateFlow()

    private val _commentsDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val commentsDownloadStatus = _commentsDownloadStatus.asStateFlow()

    private val _lists = MutableStateFlow<List<DomainSharedListModel>>(emptyList())
    val list = _lists.asStateFlow()

    private val _listName = MutableStateFlow("")
    val listName = _listName.asStateFlow()

    private val _listMovies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val movies = _listMovies.asStateFlow()

    private val _movie = MutableStateFlow<DomainSelectedMovieModel?>(null)
    val movie = _movie.asStateFlow()

    private val _comments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

    private val _successfulResult = MutableSharedFlow<Pair<String, String>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val successfulResult = _successfulResult.asSharedFlow()

    fun addComment(listId: String, movieId: Int, username: String, commentUser: String) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "",
                    username = username,
                    commentText = commentUser,
                    timestamp = System.currentTimeMillis()
                )
                commentsUseCases.addComment(listId, movieId, comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updateComment(listId: String, movieId: Int, commentId: String, userName: String, newCommentText: String) {
        viewModelScope.launch {
            try {
                val selectedComment = DomainCommentModel(
                    commentId = commentId,
                    username = userName,
                    commentText = newCommentText,
                    timestamp = System.currentTimeMillis()
                )
                commentsUseCases.updateComment(listId, movieId, commentId, selectedComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getComments(listId: String, movieId: Int) {
        viewModelScope.launch {
            try {
                _comments.value = commentsUseCases.getComments(listId, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeComments(listId: String, movieId: Int) {
        viewModelScope.launch {
            commentsUseCases.observeComments(listId, movieId) { onCommentsUpdated ->
                _comments.value = onCommentsUpdated
            }
        }
    }

    fun getLists(userId: String) {
        viewModelScope.launch {
            try {
                _lists.value = listsUseCases.getLists(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getListName(listId: String) {
        viewModelScope.launch {
            try {
                _listName.value = listsUseCases.getListName(listId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun createList(title: String, userCreatorId: String, invitedUserAddress: List<String>) {
        val source = simpleTransliterate(formatTextWithUnderscores(title))
        val sharedID = UUID.randomUUID().toString()

        val newList = DomainSharedListModel(
            listId = sharedID,
            title = title,
            source = source,
            users = "",
            timestamp = System.currentTimeMillis()
        )
        val forProfile = DomainMySharedListModel(
            listId = sharedID,
            title = title,
            source = source
        )

        viewModelScope.launch {
            try {
                listsUseCases.addList(newList, forProfile, userCreatorId, invitedUserAddress)
                _toastMessage.emit(R.string.list_created)
                _successfulResult.emit(sharedID to title)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun removeList(listId: String) {
        viewModelScope.launch {
            try {
                listsUseCases.removeList(listId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeLists(userId: String) {
        viewModelScope.launch {
            listsUseCases.observeLists(userId) { onListsUpdated ->
                _lists.value = onListsUpdated
            }
        }
    }


    fun getMovieById(listId: String, movieId: Int) {
        viewModelScope.launch {
            try {
                _movie.value = moviesUseCases.getMovieById(listId, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun addMovie(listId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                if (moviesUseCases.getMovies(listId).any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    moviesUseCases.addMovie(listId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added_to_list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun removeMovie(listId: String, movieId: Int) {
        viewModelScope.launch {
            try {
                moviesUseCases.removeMovie(listId, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovies(listId: String) {
        viewModelScope.launch {
            try {
                _listMovies.value = moviesUseCases.getMovies(listId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeMovies(listId: String) {
        viewModelScope.launch {
            moviesUseCases.observeListMovies(listId) { onMoviesUpdated ->
                _listMovies.value = onMoviesUpdated
            }
        }
    }



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

    public override fun onCleared() {
        commentsUseCases.observeComments.removeListener()
        listsUseCases.observeLists.removeListener()
        moviesUseCases.observeListMovies.removeListener()
    }
}
