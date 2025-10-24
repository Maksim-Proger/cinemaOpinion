package com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.DomainUserModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.GetUserDataUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.GetUsersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.UpdateSpecificFieldUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.user.UpdatingUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val updatingUserDataUseCase: UpdatingUserDataUseCase,
    private val updateSpecificFieldUseCase: UpdateSpecificFieldUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    ) : ViewModel() {

    private val _userData = MutableStateFlow<DomainUserModel?>(null)
    val userData = _userData.asStateFlow()

    private val _seasonalEventPoints = MutableStateFlow(0L)
    val seasonalEventPoints = _seasonalEventPoints.asStateFlow()

    private val _listAwards = MutableStateFlow("")
    val listAwards = _listAwards.asStateFlow()

    fun getUserData(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                _userData.value = userData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun updatingUserData(userId: String, nikName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val newUser = DomainUserModel(
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
                userData?.let { _seasonalEventPoints.value = it.seasonalEventPoints }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getAwardsList(userId: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let { _listAwards.value = it.awards }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun updateAwardsList(userId: String, newAward: String) {
        viewModelScope.launch {
            try {
                val userData = getUserDataUseCase(userId)
                userData?.let {
                    val currentAwards = it.awards

                    // Проверяем, есть ли уже такая награда в списке
                    if (currentAwards.contains(newAward)) {
                        return@launch
                    }

                    val updatedAwards = if (currentAwards.isEmpty()) newAward
                    else "$currentAwards,$newAward"

                    updateSpecificField(userId, "awards", updatedAwards)
                }
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
                updateAwardsList(userId, R.drawable.half_done.toString())
            }

            if (points == 80L) {
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