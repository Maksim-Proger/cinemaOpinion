package com.example.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.usecases.users.AddUserUseCase
import com.example.auth.domain.usecases.users.AuthorizationUseCase
import com.example.core.domain.DomainUserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
    private val authorizationUseCase: AuthorizationUseCase
) : ViewModel() {

    private val _authResult = MutableStateFlow<Result<Unit>?>(null)
    val authResult = _authResult.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _userData = MutableStateFlow<DomainUserModel?>(null)
    val userData = _userData.asStateFlow()

    fun addUser(nikName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val newDomainUserModel = DomainUserModel(
                    UUID.randomUUID().toString(),
                    nikName,
                    email,
                    password
                )
                addUserUseCase(newDomainUserModel)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun authorization(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = authorizationUseCase(email, password)
                _userData.value = user
                _authResult.value = Result.success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _authResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetAuthResult() {
        _authResult.value = null
    }

}