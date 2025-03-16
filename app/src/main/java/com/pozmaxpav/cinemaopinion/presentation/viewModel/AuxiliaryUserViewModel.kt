package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.AddUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.CheckLoginAndPasswordUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetUsersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.UpdateSpecificFieldUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.UpdatingUserDataUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuxiliaryUserViewModel @Inject constructor(
    private val updatingUserDataUseCase: UpdatingUserDataUseCase,
    private val updateSpecificFieldUseCase: UpdateSpecificFieldUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val checkLoginAndPasswordUseCase: CheckLoginAndPasswordUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users = _users.asStateFlow()

    private val _loginVerificationResult = MutableStateFlow<User?>(null)
    val loginVerificationResult = _loginVerificationResult.asStateFlow()

    private val _showToast = MutableStateFlow(false)
    val showToast = _showToast.asStateFlow()

    private val _userData = MutableStateFlow<User?>(null)
    val userData = _userData.asStateFlow()

    private val _seasonalEventPoints = MutableStateFlow(0L)
    val seasonalEventPoints = _seasonalEventPoints.asStateFlow()

    private val _listAwards = MutableStateFlow("")
    val listAwards = _listAwards.asStateFlow()

    fun getUsers() {
        viewModelScope.launch {
            try {
                val user = getUsersUseCase()
                _users.value = user

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addUser(nikName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    UUID.randomUUID().toString(),
                    nikName,
                    email,
                    password
                )
                addUserUseCase(newUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkLoginAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginVerification = checkLoginAndPasswordUseCase(email, password)
                _loginVerificationResult.value = loginVerification
                _showToast.value = loginVerification == null
            } catch (e: Exception) {
                e.printStackTrace()
                _showToast.value = true
            }
        }
    }

    fun resetToastState() {
        _showToast.value = false
    }

    fun getUserData(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
//                Log.d("@@@", "Из getUserData = $userData")
                _userData.value = userData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatingUserData(userId: String, nikName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    userId,
                    nikName,
                    email,
                    password
                )
                updatingUserDataUseCase(newUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSeasonalEventPoints(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let {
                    _seasonalEventPoints.value = it.seasonalEventPoints
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAwardsList(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
//                Log.d("@@@", "Из getAwardsList = $userData")
                userData?.let {
                    _listAwards.value = it.awards.replace(",", " ")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateAwardsList(userId: String, newAward: String) {
        viewModelScope.launch {
            try {
                val currentAwards = _listAwards.value
                val updatedAwards = "$currentAwards,$newAward"
                updateSpecificField(userId, "awards", updatedAwards)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatingEventData(userId: String) {
        viewModelScope.launch {
            var points = _seasonalEventPoints.value

            if (_seasonalEventPoints.value < 80L) {
                points += 20L
                _seasonalEventPoints.value = points
                updateSpecificField(userId, "seasonalEventPoints", points)
            }

            if (points == 40L) {
                getAwardsList(userId)
                updateAwardsList(userId, R.drawable.half_done.toString())
            }

            if (points == 80L) {
                getAwardsList(userId)
                updateAwardsList(userId, R.drawable.complete_passage.toString())
            }
        }
    }

    private fun updateSpecificField(userId: String, fieldName: String, newValue: Any) {
        viewModelScope.launch {
            try {
                updateSpecificFieldUseCase(userId, fieldName, newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}