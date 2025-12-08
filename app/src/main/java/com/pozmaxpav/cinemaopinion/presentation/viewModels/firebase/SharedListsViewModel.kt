package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.state.LoadingState
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainNotificationModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification.CreateNotificationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification.GetNotificationsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.notification.RemoveNotificationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.comments.AddCommentUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.movies.AddMovieUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.lists.CreatingListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.comments.GetCommentsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.movies.GetMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.lists.GetListsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.lists.RemoveListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.movies.RemoveMovieUseCase
import com.pozmaxpav.cinemaopinion.utilits.deletingOldRecords
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
    private val creatingListUseCase: CreatingListUseCase,
    private val removeListUseCase: RemoveListUseCase,
    private val getListsUseCase: GetListsUseCase,

    private val addMovieUseCase: AddMovieUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val removeMovieUseCase: RemoveMovieUseCase,

    private val addCommentUseCase: AddCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,

    private val createNotificationUseCase: CreateNotificationUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val removeNotificationUseCase: RemoveNotificationUseCase,
) : ViewModel() {

    private val _movieDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val movieDownloadStatus = _movieDownloadStatus.asStateFlow()

    private val _commentsDownloadStatus = MutableStateFlow<LoadingState>(LoadingState.Success)
    val commentsDownloadStatus = _commentsDownloadStatus.asStateFlow()

    private val _notifications = MutableStateFlow<List<DomainNotificationModel>>(emptyList())
    val notifications = _notifications.asStateFlow()

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

    fun getLists(userId: String) {
        viewModelScope.launch {
            try {
                val myList = getListsUseCase(userId)
                _lists.value = myList
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
                creatingListUseCase(newList, forProfile, userCreatorId, invitedUserAddress)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun removeList(listId: String) {
        viewModelScope.launch {
            try {
                removeListUseCase(listId)
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
    fun removeMovie(listId: String, movieId: Int) {
        viewModelScope.launch {
            try {
                removeMovieUseCase(listId, movieId)
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

    fun createNotification(
        context: Context,
        entityId: Int = 0,
        sharedListId: String = "",
        username: String,
        stringResourceId: Int,
        title: String
    ) {
        viewModelScope.launch {
            try {
                val stringResource = context.getString(stringResourceId)
                val noteText = "$stringResource $title"
                val note = DomainNotificationModel(
                    noteId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    entityId = entityId,
                    sharedListId = sharedListId,
                    username = username,
                    noteText = noteText,
                    timestamp = System.currentTimeMillis()
                )
                createNotificationUseCase(note)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getNotifications(userId: String) {
        viewModelScope.launch {
            try {
                _notifications.value = getNotificationsUseCase(userId)
                removeOldNotifications() // Удаляем старые записи
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun removeNotification(id: String) {
        viewModelScope.launch {
            try {
                removeNotificationUseCase(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun removeOldNotifications() { // Удаление записей из базы данных и списка
        viewModelScope.launch {
            val currentList = _notifications.value
            val filteredList = mutableListOf<DomainNotificationModel>()

            currentList.forEach { record ->
                if (deletingOldRecords(record.timestamp)) {
                    // Удаляем запись из базы данных
                    removeNotification(record.noteId)
                } else {
                    filteredList.add(record)
                }
            }

            _notifications.value = filteredList // Обновляем список после удаления
        }
    }
}
