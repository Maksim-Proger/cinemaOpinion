package com.pozmaxpav.cinemaopinion.presentation.viewModel.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieSearchList2
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieTopList
import com.pozmaxpav.cinemaopinion.domain.models.api.information.Information
import com.pozmaxpav.cinemaopinion.domain.models.api.news.NewsList
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetPremiereMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchFilmsByFiltersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMovieByIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMovies2UseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetSearchMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.movies.GetTopMoviesUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.information.GetMovieInformationUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.api.news.GetMediaNewsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetRegistrationFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetUserIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveRegistrationFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveUserIdUseCase
import com.pozmaxpav.cinemaopinion.utilits.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val saveAppVersionUseCase: SaveAppVersionUseCase,
    private val getResultCheckingUseCase: GetResultCheckingUseCase,
    private val saveResultCheckingUseCase: SaveResultCheckingUseCase,
    private val saveRegistrationFlagUseCase: SaveRegistrationFlagUseCase,
    private val getRegistrationFlagUseCase: GetRegistrationFlagUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _versionApp = MutableStateFlow("Unknown")
    val versionApp = _versionApp.asStateFlow()

    private val _resultChecking = MutableStateFlow(false)
    val resultChecking = _resultChecking.asStateFlow()

    private val _registrationFlag = MutableStateFlow(false)
    val registrationFlag = _registrationFlag.asStateFlow()

    private val _userId = MutableStateFlow("Unknown")
    val userId = _userId.asStateFlow()

    init {
        getAppVersion()
        getResultChecking()
        getRegistrationFlag()
        getUserId()
    }

    fun saveRegistrationFlag(registrationFlag: Boolean) {
        viewModelScope.launch {
            try {
                saveRegistrationFlagUseCase(registrationFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun getRegistrationFlag() {
        viewModelScope.launch {
            try {
                val flag = getRegistrationFlagUseCase()
                _registrationFlag.value = flag
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun saveUserId(userId: String) {
        viewModelScope.launch {
            try {
                saveUserIdUseCase(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun getUserId() {
        viewModelScope.launch {
            try {
                val id = getUserIdUseCase() ?: "Unknown"
                _userId.value = id
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetResultChecking() {
        _resultChecking.value = true
    }
    fun saveAppVersion(version: String) {
        viewModelScope.launch {
            try {
                saveAppVersionUseCase(version)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun getAppVersion() {
        viewModelScope.launch {
            try {
                val version = getAppVersionUseCase() ?: "Unknown"
                _versionApp.value = version
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun saveResultChecking(resultChecking: Boolean) {
        viewModelScope.launch {
            saveResultCheckingUseCase(resultChecking)
        }
    }
    private fun getResultChecking() {
        viewModelScope.launch {
            try {
                val result = getResultCheckingUseCase()
                _resultChecking.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}








