package com.pozmaxpav.cinemaopinion.presentation.viewModels.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.usecases.system.GetUserIdUseCase
import com.example.auth.domain.usecases.system.SaveUserIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveResultCheckingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SystemViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val saveAppVersionUseCase: SaveAppVersionUseCase,
    private val getResultCheckingUseCase: GetResultCheckingUseCase,
    private val saveResultCheckingUseCase: SaveResultCheckingUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _versionApp = MutableStateFlow("Unknown")
    val versionApp = _versionApp.asStateFlow()

    private val _resultChecking = MutableStateFlow(false)
    val resultChecking = _resultChecking.asStateFlow()

    private val _userId = MutableStateFlow("Unknown")
    val userId = _userId.asStateFlow()

    init {
        getAppVersion()
        getResultChecking()
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

    fun getUserId() {
        viewModelScope.launch {
            try {
                val id = getUserIdUseCase()
                _userId.value = id ?: "Unknown"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
                _resultChecking.value = getResultCheckingUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetResultChecking() {
        _resultChecking.value = true
    }

}








