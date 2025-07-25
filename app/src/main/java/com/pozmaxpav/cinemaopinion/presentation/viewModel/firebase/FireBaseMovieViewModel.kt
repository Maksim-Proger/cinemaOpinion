package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainChangelogModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.GetMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.GetCommentsForMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.GetMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.ObserveCommentsForMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.ObserveListMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.SaveMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.SendingToNewDirectoryUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.movies.comments.UpdateCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.GetRecordsOfChangesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.RemoveRecordsOfChangesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.records.SavingChangeRecordUseCase
import com.pozmaxpav.cinemaopinion.utilits.deletingOldRecords
import com.pozmaxpav.cinemaopinion.utilits.state.State
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
class FireBaseMovieViewModel @Inject constructor(
    private val saveMovieUseCase: SaveMovieUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,
    private val getMovieUseCase: GetMovieUseCase,
    private val observeListMoviesUseCase: ObserveListMoviesUseCase,
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsForMovieUseCase: GetCommentsForMovieUseCase,
    private val observeCommentsForMovieUseCase: ObserveCommentsForMovieUseCase,
    private val updateCommentUseCase: UpdateCommentUseCase,
    private val savingChangeRecordUseCase: SavingChangeRecordUseCase,
    private val getRecordsOfChangesUseCase: GetRecordsOfChangesUseCase,
    private val removeRecordsOfChangesUseCase: RemoveRecordsOfChangesUseCase,
    private val sendingToNewDirectoryUseCase: SendingToNewDirectoryUseCase
) : ViewModel() {

    private val _movieDownloadStatus = MutableStateFlow<State>(State.Success)
    val movieDownloadStatus = _movieDownloadStatus.asStateFlow()

    private val _commentsDownloadStatus = MutableStateFlow<State>(State.Success)
    val commentsDownloadStatus = _commentsDownloadStatus.asStateFlow()

    private val _movies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _comments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _listOfChanges = MutableStateFlow<List<DomainChangelogModel>>(emptyList())
    val listOfChanges = _listOfChanges.asStateFlow()

    private val _movie = MutableStateFlow<DomainSelectedMovieModel?>(null)
    val movie = _movie.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

    fun saveMovie(dataSource: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                val movies = getMovieUseCase(dataSource)
                if (movies.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    saveMovieUseCase(dataSource, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added_to_general_list)
                }
            } catch (e: Exception) {
                _toastMessage.emit(R.string.movie_has_already_been_added)
                e.printStackTrace()
            }
        }
    }
    fun getMovies(dataSource: String) {
        viewModelScope.launch {
            _movieDownloadStatus.value = State.Loading
            try {
                val moviesList = getMovieUseCase(dataSource)
                _movies.value = moviesList
                delay(500)
                _movieDownloadStatus.value = State.Success
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMovieById(dataSource: String, movieId: Int) {
        viewModelScope.launch {
            try {
                val movie = getMovieByIdUseCase(dataSource, movieId)
                _movie.value = movie
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
    fun removeMovie(dataSource: String, movieId: Int) {
        viewModelScope.launch {
            try {
                removeMovieUseCase(dataSource, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
    fun savingChangeRecord(
        context: Context,
        username: String,
        stringResourceId: Int,
        title: String,
        newDataSource: String,
        entityId: Int = 0
    ) {
        viewModelScope.launch {
            try {
                val stringResource = context.getString(stringResourceId)
                val noteText = "$stringResource $title"
                val note = DomainChangelogModel(
                    noteId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    entityId = entityId,
                    newDataSource = newDataSource,
                    username = username,
                    noteText = noteText,
                    timestamp = System.currentTimeMillis()
                )

                savingChangeRecordUseCase(note)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun removeRecordsOfChanges(id: String) {
        viewModelScope.launch {
            try {
                removeRecordsOfChangesUseCase(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun removeOldRecords() { // Удаление записей из базы данных и списка
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

            _listOfChanges.value = filteredList // Обновляем список после удаления
        }
    }

    fun addComment(dataSource: String, movieId: Double, username: String, commentUser: String) {
        val comment = DomainCommentModel(
            commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
            username = username,
            commentText = commentUser,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            try {
                addCommentUseCase(dataSource, movieId, comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getComments(dataSource: String, movieId: Int) {
        viewModelScope.launch {
            _commentsDownloadStatus.value = State.Loading
            try {
                val commentList = getCommentsForMovieUseCase(dataSource, movieId)
                _comments.value = commentList
                delay(500)
                _commentsDownloadStatus.value = State.Success
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun observeComments(dataSource: String, movieId: Int) {
        viewModelScope.launch {
            observeCommentsForMovieUseCase(dataSource, movieId) { updatedComments ->
                _comments.value = updatedComments
            }
        }
    }
    fun updateComment(dataSource: String, userName: String, selectedMovieId: Int, commentId: String, newCommentText: String) {
        viewModelScope.launch {
            try {
                val selectedComment = DomainCommentModel(
                    commentId,
                    userName,
                    newCommentText,
                    System.currentTimeMillis()
                )

                updateCommentUseCase(dataSource, selectedMovieId, commentId, selectedComment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendingToNewDirectory(dataSource: String, directionDataSource: String, movieId: Double) {
        viewModelScope.launch {
            try {
                sendingToNewDirectoryUseCase(dataSource, directionDataSource, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public override fun onCleared() {
        observeListMoviesUseCase.removeListener()
        observeCommentsForMovieUseCase.removeListener()
        super.onCleared()
    }
}

