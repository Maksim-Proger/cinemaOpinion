package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedFilm.DeleteMovieFromPersonalListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedFilm.GetListPersonalMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.selectedFilm.AddMovieToPersonalListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectedMovieViewModel @Inject constructor(
    private val getListSelectedFilmsUseCase: GetListPersonalMoviesUseCase,
    private val insertFilmUseCase: AddMovieToPersonalListUseCase,
    private val getFilmById: GetFilmByIdUseCase,
    private val deleteSelectedFilmUseCase: DeleteMovieFromPersonalListUseCase
) : ViewModel() {

    private val _selectedMovies = MutableStateFlow<List<SelectedMovie>>(emptyList())
    val selectedMovies: StateFlow<List<SelectedMovie>> = _selectedMovies

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    init {
        fitchListSelectedMovies()
    }

    fun fitchListSelectedMovies() {
        viewModelScope.launch {
            try {
                val movies = getListSelectedFilmsUseCase()
                _selectedMovies.value = movies
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun addSelectedMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            try {
                val existMovie = getFilmById(selectedMovie.id)
                if (existMovie == null) {
                    insertFilmUseCase.invoke(selectedMovie)
                } else {
                    _status.value = "error"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSelectedMovie(selectedMovie: SelectedMovie) {
        viewModelScope.launch {
            try {
                deleteSelectedFilmUseCase(selectedMovie)
                fitchListSelectedMovies()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}