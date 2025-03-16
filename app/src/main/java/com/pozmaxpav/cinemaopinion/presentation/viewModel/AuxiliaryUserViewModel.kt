package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.User
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.AddUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.CheckLoginAndPasswordUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetUsersUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.UpdateSeasonalEventPointsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.UpdatingUserDataUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.firebase.GetUserDataUseCase
import com.pozmaxpav.cinemaopinion.utilits.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuxiliaryUserViewModel @Inject constructor(
    private val updatingUserDataUseCase: UpdatingUserDataUseCase,
    private val updateSeasonalEventPointsUseCase: UpdateSeasonalEventPointsUseCase,
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

    private val _listAwards = MutableStateFlow<List<String>>(emptyList())
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
                _userData.value = userData
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
//        user?.let {
//            _seasonalEventPoints.value = it.seasonalEventPoints
//            _listAwards.value = it.awards
//        }
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

    fun updateSeasonalEventPoints(userId: String, fieldName: String, newValue: Any) {
        viewModelScope.launch {
            try {
                updateSeasonalEventPointsUseCase(userId, fieldName, newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun updateAwardsList(userId: String, newAward: String) {
//        viewModelScope.launch {
//            try {
//                // Получаем текущий список наград напрямую из базы через UseCase
//                val currentAwards = getAwardsUseCase(userId)
//
//                // Убираем некорректные вложенные списки или пустые значения
//                val safeAwards = currentAwards.flatMap { award ->
//                    if (award.startsWith("[") && award.endsWith("]")) {
//                        // Если элемент является JSON-строкой списка, распаковываем его
//                        Converters().toList(award)
//                    } else {
//                        listOf(award) // Оставляем элемент как есть
//                    }
//                }
//
//                // Добавляем новую награду к списку
//                val updatedAwards = safeAwards + newAward
//
//                // Обновляем список в базе данных
//                updateAwardsListUseCase(userId, updatedAwards)
//
//                // Обновляем StateFlow
//                _listAwards.value = updatedAwards
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//    fun handleEvent(userId: String) {
//        viewModelScope.launch {
//            var points = _seasonalEventPoints.value
//
//            if (_seasonalEventPoints.value < 80L) {
//                incrementSeasonalEventPoints(userId, 2L)
//                points += 2L
//            }
//
//            if (points == 40L) {
//                updateAwardsList(userId, R.drawable.half_done.toString())
//            }
//
//            if (points == 80L) {
//                updateAwardsList(userId, R.drawable.complete_passage.toString())
//            }
//        }
//    }
//
//    fun incrementSeasonalEventPoints(userId: String, increment: Long) {
//        viewModelScope.launch {
//            try {
//                incrementSeasonalEventPointsUseCase(userId, increment)
//                fitchUser()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}