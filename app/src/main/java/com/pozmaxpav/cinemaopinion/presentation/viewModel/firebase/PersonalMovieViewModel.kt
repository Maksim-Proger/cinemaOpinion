package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddCommentToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddMovieToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.DeleteMovieFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetCommentsFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetListPersonalMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveCommentsFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveListSelectedMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.SendingToNewDirectoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalMovieViewModel @Inject constructor(
    private val addMovieToPersonalListUseCase: AddMovieToPersonalListUseCase,
    private val getListPersonalMoviesUseCase: GetListPersonalMoviesUseCase,
    private val observeListSelectedMoviesUseCase: ObserveListSelectedMoviesUseCase,
    private val deleteMovieFromPersonalListUseCase: DeleteMovieFromPersonalListUseCase,
    private val addCommentToPersonalListUseCase: AddCommentToPersonalListUseCase,
    private val getCommentsFromPersonalListUseCase: GetCommentsFromPersonalListUseCase,
    private val observeCommentsForMovieFromPersonalListUseCase: ObserveCommentsFromPersonalListUseCase,
    private val sendingToNewDirectoryUseCase: SendingToNewDirectoryUseCase
    ) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val selectedMovies: StateFlow<List<DomainSelectedMovieModel>> = _selectedMovies

    private val _listComments = MutableStateFlow<List<DomainCommentModel>>(emptyList())
    val listComments = _listComments.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    fun addCommentToPersonalList(userId: String, selectedMovieId: Int, username: String, textComment: String) {
        viewModelScope.launch {
            try {
                val comment = DomainCommentModel(
                    commentId = "", // Оставляем пустым, так как key будет сгенерирован позже
                    username = username,
                    commentText = textComment,
                    timestamp = System.currentTimeMillis()
                )
                addCommentToPersonalListUseCase(userId,selectedMovieId,comment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCommentsFromPersonalList(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                val comments = getCommentsFromPersonalListUseCase(userId, selectedMovieId)
                _listComments.value = comments
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun observeCommentsForMovieFromPersonalList(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                observeCommentsForMovieFromPersonalListUseCase(userId, selectedMovieId) { onCommentsUpdated ->
                    _listComments.value = onCommentsUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getListPersonalMovies(userId: String) {
        viewModelScope.launch {
            try {
                val movies = getListPersonalMoviesUseCase(userId)
                _selectedMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun addMovieToPersonalList(userId: String, selectedMovie: DomainSelectedMovieModel) {
        viewModelScope.launch {
            try {
                addMovieToPersonalListUseCase(userId, selectedMovie)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun observeListSelectedMovies(userId: String) {
        viewModelScope.launch {
            try {
                observeListSelectedMoviesUseCase(userId) { onSelectedMoviesUpdated ->
                    _selectedMovies.value = onSelectedMoviesUpdated
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSelectedMovie(userId: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                deleteMovieFromPersonalListUseCase(userId, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendingToNewDirectory(userId: String, dataSource: String, directionDataSource: String, selectedMovieId: Int) {
        viewModelScope.launch {
            try {
                sendingToNewDirectoryUseCase(userId, dataSource, directionDataSource, selectedMovieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    public override fun onCleared() {
        observeListSelectedMoviesUseCase.removeListener()
        observeCommentsForMovieFromPersonalListUseCase.removeListener()
        super.onCleared()
    }

}