package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.usecase.user.GetUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.InsertUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<DomainUser?>(null)
    val users: StateFlow<DomainUser?> get() = _users

    fun addUser(user: DomainUser) {
        viewModelScope.launch {
            try {
                insertUserUseCase(user)
                fitchUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fitchUser() {
        viewModelScope.launch {
            try {
                val user = getUserUseCase()
                _users.value = user // user может быть null
            } catch (e: Exception) {
                _users.value = null
                e.printStackTrace()
            }
        }
    }

    fun updateUser(user: DomainUser) {
        viewModelScope.launch {
            try {
                updateUserUseCase(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}