package com.example.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.repository.system.UserPreferences
import com.example.auth.domain.usecases.system.GetRegistrationFlagUseCase
import com.example.auth.domain.usecases.system.GetUserIdUseCase
import com.example.auth.domain.usecases.system.SaveRegistrationFlagUseCase
import com.example.auth.domain.usecases.system.SaveUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SystemViewModelAuth @Inject constructor(
    private val saveRegistrationFlagUseCase: SaveRegistrationFlagUseCase,
    private val getRegistrationFlagUseCase: GetRegistrationFlagUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
) : ViewModel(),  UserPreferences {

    private val _registrationFlag = MutableStateFlow(false)
    override val registrationFlag = _registrationFlag.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    override val userId = _userId.asStateFlow()

    init {
        loadInitialValues()
    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            _registrationFlag.value = runCatching { getRegistrationFlagUseCase() }.getOrDefault(false)
            _userId.value = runCatching { getUserIdUseCase() }.getOrNull()
        }
    }

    override fun saveRegistrationFlag(registrationFlag: Boolean) {
        viewModelScope.launch {
            runCatching { saveRegistrationFlagUseCase(registrationFlag) }
        }
    }

    override fun saveUserId(userId: String) {
        viewModelScope.launch {
            runCatching { saveUserIdUseCase(userId) }
        }
    }
}