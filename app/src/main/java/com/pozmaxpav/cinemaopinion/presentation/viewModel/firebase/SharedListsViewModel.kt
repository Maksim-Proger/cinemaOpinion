package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainMySharedListModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSharedListModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.AddMovieToSpecificSharedListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.CreatingSharedListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.sharedlists.GetMovieFromSpecificSharedListUseCase
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
import kotlin.collections.any

@HiltViewModel
class SharedListsViewModel @Inject constructor(
    private val creatingSharedListUseCase: CreatingSharedListUseCase,
    private val getSharedListsUseCase: GetSharedListsUseCase,
    private val addMovieToSpecificSharedListUseCase: AddMovieToSpecificSharedListUseCase,
    private val getMovieFromSpecificSharedListUseCase: GetMovieFromSpecificSharedListUseCase
): ViewModel() {

    private val _lists = MutableStateFlow<List<DomainSharedListModel>>(emptyList())
    val list = _lists.asStateFlow()

    private val _movies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _toastMessage = MutableSharedFlow<Int>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val toastMessage = _toastMessage.asSharedFlow()

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
                val  moviesForCheck = getMovieFromSpecificSharedListUseCase(listId)
                if (moviesForCheck.any { it.id == selectedMovie.id }) {
                    _toastMessage.emit(R.string.movie_has_already_been_added)
                } else {
                    addMovieToSpecificSharedListUseCase(listId, selectedMovie)
                    _toastMessage.emit(R.string.movie_has_been_added_to_general_list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMoviesFromSpecialList(listId: String) {
        viewModelScope.launch {
            try {
                val listMovies = getMovieFromSpecificSharedListUseCase(listId)
                _movies.value = listMovies
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
