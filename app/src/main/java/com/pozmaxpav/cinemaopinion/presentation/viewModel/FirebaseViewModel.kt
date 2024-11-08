package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainComment
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetCommentsForMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.ObserveCommentsForMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.GetRecordsOfChangesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.SavingChangeRecordUseCase
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
    private val getCommentsForMovieUseCase: GetCommentsForMovieUseCase,
    private val observeCommentsForMovieUseCase: ObserveCommentsForMovieUseCase,
    private val savingChangeRecordUseCase: SavingChangeRecordUseCase,
    private val getRecordsOfChangesUseCase: GetRecordsOfChangesUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val movies: StateFlow<List<SelectedMovie>> = _movies

    private val _comments = MutableStateFlow<List<DomainComment>>(emptyList())
    val comments: StateFlow<List<DomainComment>> = _comments

    private val _listOfChanges = MutableStateFlow<List<DomainChangelogModel>>(emptyList())
    val listOfChanges: StateFlow<List<DomainChangelogModel>> = _listOfChanges

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun getRecordsOfChanges() {
        viewModelScope.launch {
            try {
                val list = getRecordsOfChangesUseCase()
                _listOfChanges.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun savingChangeRecord(username: String, noteText: String) {
        val note = DomainChangelogModel(
            noteId = "", // Оставляем пустым, так как key будет сгенерирован позже
            username = username,
            noteText = noteText,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            try {
                savingChangeRecordUseCase(note)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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

    fun observeComments(movieId: Double) {
        viewModelScope.launch {
            observeCommentsForMovieUseCase(movieId) { updatedComments ->
                _comments.value = updatedComments
            }
        }
    }

    fun addComment(
        movieId: Double,
        username: String,
        commentUser: String
    ) {
        val comment = DomainComment(
            commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
            username = username,
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