package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.AddMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.CreatingSharedListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.GetCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.GetMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.GetSharedListsUseCase
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
    private val creatingSharedListUseCase: CreatingSharedListUseCase,
    private val getSharedListsUseCase: GetSharedListsUseCase,
    private val addMovieUseCase: AddMovieUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase
) : ViewModel() {

    private val _lists = MutableStateFlow<List<DomainSharedListModel>>(emptyList())
    val list = _lists.asStateFlow()

    private val _movies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _comments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

    fun addComment(listId: String, movieId: Int, username: String, commentUser: String) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "",
                    username = username,
                    commentText = commentUser,
                    timestamp = System.currentTimeMillis()
                )
                addCommentUseCase(listId, movieId, comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getComments(listId: String, movieId: Int) {
        viewModelScope.launch {
            try {
                _comments.value = getCommentsUseCase(listId, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createList(title: String, userCreatorId: String, invitedUserAddress: String) {
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
                creatingSharedListUseCase(newList, forProfile, userCreatorId, invitedUserAddress)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getLists(userId: String) {
        viewModelScope.launch {
            try {
                val myList = getSharedListsUseCase(userId)
                _lists.value = myList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addMovie(listId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                val moviesForCheck = getMoviesUseCase(listId)
                if (moviesForCheck.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    addMovieUseCase(listId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added_to_general_list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovies(listId: String) {
        viewModelScope.launch {
            try {
                val listMovies = getMoviesUseCase(listId)
                _movies.value = listMovies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
