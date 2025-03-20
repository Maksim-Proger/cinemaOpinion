package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie.DeleteMovieFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie.GetListPersonalMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie.AddMovieToPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedmovie.ObserveListSelectedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedMovieViewModel @Inject constructor(
    private val addMovieToPersonalListUseCase: AddMovieToPersonalListUseCase,
    private val getListPersonalMoviesUseCase: GetListPersonalMoviesUseCase,
    private val observeListSelectedMoviesUseCase: ObserveListSelectedMoviesUseCase,
    private val deleteMovieFromPersonalListUseCase: DeleteMovieFromPersonalListUseCase
) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<SelectedMovieModel>>(emptyList())
    val selectedMovies: StateFlow<List<SelectedMovieModel>> = _selectedMovies

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

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

    fun addMovieToPersonalList(userId: String, selectedMovie: SelectedMovieModel) {
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

    fun deleteSelectedMovie(userId: String, selectedMovieId: String) {
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