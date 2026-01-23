package com.pozmaxpav.cinemaopinion.presentation.viewModels.system

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.usecase.system.ClearUserDataUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetPushTokenUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetRegistrationFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.GetUserIdUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveAppVersionUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SavePushTokenUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveRegistrationFlagUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveResultCheckingUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.system.SaveUserIdUseCase
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
    private val getUserIdUseCase: GetUserIdUseCase,
    private val saveRegistrationFlagUseCase: SaveRegistrationFlagUseCase,
    private val getRegistrationFlagUseCase: GetRegistrationFlagUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val savePushTokenUseCase: SavePushTokenUseCase,
    private val getPushTokenUseCase: GetPushTokenUseCase
) : ViewModel() {

    private val _versionApp = MutableStateFlow("Unknown")
    val versionApp = _versionApp.asStateFlow()

    private val _resultChecking = MutableStateFlow(false)
    val resultChecking = _resultChecking.asStateFlow()

    private val _userId = MutableStateFlow("Unknown")
    val userId = _userId.asStateFlow()

    private val _registrationFlag = MutableStateFlow(false)
    val registrationFlag = _registrationFlag.asStateFlow()

    private val _pushToken = MutableStateFlow("Unknown")
    val pushToken = _pushToken.asStateFlow()

    init {
        getRegistrationFlag()
        getAppVersion()
        getResultChecking()
    }

    fun saveRegistrationFlag(registrationFlag: Boolean) {
        viewModelScope.launch {
            runCatching { saveRegistrationFlagUseCase(registrationFlag) }
        }
    }

    fun getRegistrationFlag() {
        viewModelScope.launch {
            _registrationFlag.value = runCatching {
                getRegistrationFlagUseCase()
            }.getOrDefault(false)
        }
    }

    fun savePushToken(pushToken: String) {
        viewModelScope.launch {
            try {
                savePushTokenUseCase(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPushToken() {
        viewModelScope.launch {
            try {
                _pushToken.value = getPushTokenUseCase() ?: "Unknown"
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

    fun clearUserData() {
        viewModelScope.launch {
            runCatching { clearUserDataUseCase() }
        }
    }

}

