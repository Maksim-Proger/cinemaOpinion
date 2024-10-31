package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetCommentsForMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsForMovieUseCase: GetCommentsForMovieUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val movies: StateFlow<List<SelectedMovie>> = _movies

    private val _comments = MutableStateFlow<List<DomainComment>>(emptyList())
    val comments: StateFlow<List<DomainComment>> = _comments

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getComments(movieId: Double) {
        viewModelScope.launch {
            try {
                val commentList = getCommentsForMovieUseCase(movieId)
                _comments.value = commentList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addComment(movieId: Double, commentUser: String) {
        val comment = DomainComment(
            commentId = "", // TODO: Как-то странно это!!!!
            username = "МаксимМаксим",
            commentText = commentUser,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            try {
                addCommentUseCase(movieId, comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMovies() {
        viewModelScope.launch {
            try {
                val moviesList = getMovieUseCase()
                _movies.value = moviesList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun saveMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            try {
                saveMovieUseCase(selectedMovie)
                _errorMessage.value = null // Зачем это нам?
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun removeMovie(id: Double) {
        viewModelScope.launch {
            try {
                removeMovieUseCase(id)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}