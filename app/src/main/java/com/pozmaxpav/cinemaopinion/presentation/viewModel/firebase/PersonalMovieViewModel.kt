package com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainCommentModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddCommentToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.DeleteMovieFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetListPersonalMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.AddMovieToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.GetCommentsFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.personalmovie.ObserveListSelectedMoviesUseCase
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
//    private val observeCommentsForMovieFromPersonalListUseCase: ObserveCommentsFromPersonalListUseCase
    ) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val selectedMovies: StateFlow<List<DomainSelectedMovieModel>> = _selectedMovies

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

    public override fun onCleared() {
        super.onCleared()
        observeListSelectedMoviesUseCase.removeListener()
    }

}