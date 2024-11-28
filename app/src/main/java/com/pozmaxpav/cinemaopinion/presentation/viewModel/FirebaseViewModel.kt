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
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.ObserveListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SaveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.SendingToTheViewedFolderUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.GetRecordsOfChangesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.RemoveRecordsOfChangesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.SavingChangeRecordUseCase
import com.pozmaxpav.cinemaopinion.utilits.deletingOldRecords
import com.pozmaxpav.cinemaopinion.utilits.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase,
    private val observeListMoviesUseCase: ObserveListMoviesUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsForMovieUseCase: GetCommentsForMovieUseCase,
    private val observeCommentsForMovieUseCase: ObserveCommentsForMovieUseCase,
    private val savingChangeRecordUseCase: SavingChangeRecordUseCase,
    private val getRecordsOfChangesUseCase: GetRecordsOfChangesUseCase,
    private val removeRecordsOfChangesUseCase: RemoveRecordsOfChangesUseCase,
    private val sendingToTheViewedFolderUseCase: SendingToTheViewedFolderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _movies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val movies: StateFlow<List<SelectedMovie>> = _movies

    private val _comments = MutableStateFlow<List<DomainComment>>(emptyList())
    val comments: StateFlow<List<DomainComment>> = _comments

    private val _listOfChanges = MutableStateFlow<List<DomainChangelogModel>>(emptyList())
    val listOfChanges: StateFlow<List<DomainChangelogModel>> = _listOfChanges

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun sendingToTheViewedFolder(movieId: Double) {
        viewModelScope.launch {
            try {
                sendingToTheViewedFolderUseCase(movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeRecordsOfChanges(id: String) {
        viewModelScope.launch {
            try {
                removeRecordsOfChangesUseCase(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Удаление записей из базы данных и списка
    private fun removeOldRecords() {
        viewModelScope.launch {
            val currentList = _listOfChanges.value
            val filteredList = mutableListOf<DomainChangelogModel>()

            currentList.forEach { record ->
                if (deletingOldRecords(record.timestamp)) {
                    // Удаляем запись из базы данных
                    removeRecordsOfChanges(record.noteId)
                } else {
                    filteredList.add(record)
                }
            }

            // Обновляем список после удаления
            _listOfChanges.value = filteredList
        }
    }

    fun getRecordsOfChanges() {
        viewModelScope.launch {
            try {
                val list = getRecordsOfChangesUseCase()
                _listOfChanges.value = list
                removeOldRecords() // Удаляем старые записи
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

    fun getComments(dataSource: String, movieId: Double) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val commentList = getCommentsForMovieUseCase(dataSource, movieId)
                _comments.value = commentList
                _state.value = State.Success
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun observeComments(dataSource: String, movieId: Double) {
        viewModelScope.launch {
            observeCommentsForMovieUseCase(dataSource, movieId) { updatedComments ->
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

    fun observeListMovies(dataSource: String) {
        viewModelScope.launch {
            observeListMoviesUseCase(dataSource) { updateListMovies ->
                _movies.value = updateListMovies
            }
        }
    }

    fun getMovies(dataSource: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            try {
                val moviesList = getMovieUseCase(dataSource)
                _movies.value = moviesList
                _state.value = State.Success
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun saveMovie(dataSource: String, selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            try {
                saveMovieUseCase(dataSource, selectedMovie)
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

