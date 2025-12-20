package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.systemlist.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SystemMovieViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val _list = MutableStateFlow<List<DomainSelectedMovieModel>>(emptyList())
    val list = _list.asStateFlow()

    fun getMovies(dataSource: String) {
        viewModelScope.launch {
            try {
                _list.value = getMoviesUseCase(dataSource)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}