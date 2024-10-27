package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.User
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

    private val _users = MutableStateFlow<User?>(null)
    val users: StateFlow<User?> get() = _users

    fun addUser(firstName: String, lastName: String) {
        val newUser = User(0, firstName, lastName)
        viewModelScope.launch {
            try {
                insertUserUseCase(newUser)
                fitchUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: Разобраться что тут у меня!
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

    fun updateUser(firstName: String, lastName: String) {
        viewModelScope.launch {
            try {
                val user = getUserUseCase()
                val currentUser = user?.id
                if (currentUser != null) {
                    val updatedUser = User(currentUser, firstName, lastName)
                    updateUserUseCase.invoke(updatedUser)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}