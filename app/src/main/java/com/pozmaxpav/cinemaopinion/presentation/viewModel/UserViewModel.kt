package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.User
import com.pozmaxpav.cinemaopinion.domain.usecase.GetUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.InsertUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.UpdateUserUseCase
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

    private val _users = MutableStateFlow<User?>(null)
    val users: StateFlow<User?> get() = _users

    fun addUser(firstName: String, lastName: String) {
        val newUser = User(0, firstName, lastName)
        viewModelScope.launch {
            insertUserUseCase(newUser)
            fitchUser()
        }
    }

    fun fitchUser() {
        viewModelScope.launch {
            try {
                val user = getUserUseCase()
                _users.value = user // user может быть null
            } catch (e: Exception) {
                _users.value = null
            }
        }
    }

    fun updateUser(firstName: String, lastName: String) {
        viewModelScope.launch {
            val user = getUserUseCase()
            val currentUser = user?.id
            if (currentUser != null) {
                val updatedUser = User(currentUser, firstName, lastName)
                updateUserUseCase.invoke(updatedUser)
            }
        }
    }

}