package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

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
                addMovieToSpecificSharedListUseCase(listId, selectedMovie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getMoviesFroSpecialList(listId: String) {
        viewModelScope.launch {
            try {
                val moviesResult = getMovieFromSpecificSharedListUseCase(listId)
                _movies.value = moviesResult
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}