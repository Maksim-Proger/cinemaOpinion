package com.pozmaxpav.cinemaopinion.presentation.viewModel

import android.util.Log
import com.pozmaxpav.cinemaopinion.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pozmaxpav.cinemaopinion.data.localdb.converters.Converters
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.usecase.user.GetAwardsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.GetUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.IncrementSeasonalEventPointsUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.InsertUserUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.UpdateAwardsListUseCase
import com.pozmaxpav.cinemaopinion.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val insertUserUseCase: InsertUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val incrementSeasonalEventPointsUseCase: IncrementSeasonalEventPointsUseCase,
    private val getAwardsUseCase: GetAwardsUseCase,
    private val updateAwardsListUseCase: UpdateAwardsListUseCase
) : ViewModel() {

    private val _users = MutableStateFlow<DomainUser?>(null)
    val users: StateFlow<DomainUser?> get() = _users.asStateFlow()

    private val _seasonalEventPoints = MutableStateFlow(0L)
    val seasonalEventPoints: StateFlow<Long> = _seasonalEventPoints.asStateFlow() // TODO: Что такое .asStateFlow()

    private val _listAwards = MutableStateFlow<List<String>>(emptyList())
    val listAwards: StateFlow<List<String>> = _listAwards.asStateFlow()

    fun handleEvent(userId: String) {
        viewModelScope.launch {
            var points = _seasonalEventPoints.value

            if (_seasonalEventPoints.value < 80L) {
                incrementSeasonalEventPoints(userId, 40L)
                points += 40L
            }

            Log.d("@@@", "Текущее значение очков: $points")

            if (points == 40L) {
                Log.d("@@@", "Список из аккаунта: ${R.drawable.half_done}")
                updateAwardsList(userId, R.drawable.half_done.toString())
            }

            if (points == 80L) {
                Log.d("@@@", "Список из аккаунта: ${R.drawable.complete_passage}")
                updateAwardsList(userId, R.drawable.complete_passage.toString())
            }
        }
    }


    fun incrementSeasonalEventPoints(userId: String, increment: Long) {
        viewModelScope.launch {
            try {
                incrementSeasonalEventPointsUseCase(userId, increment)
                fitchUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateAwardsList(userId: String, newAward: String) {
        viewModelScope.launch {
            try {
                // Получаем текущий список наград напрямую из базы через UseCase
                val currentAwards = getAwardsUseCase(userId)

                // Убираем некорректные вложенные списки или пустые значения
                val safeAwards = currentAwards.flatMap { award ->
                    if (award.startsWith("[") && award.endsWith("]")) {
                        // Если элемент является JSON-строкой списка, распаковываем его
                        Converters().toList(award)
                    } else {
                        listOf(award) // Оставляем элемент как есть
                    }
                }

                // Добавляем новую награду к списку
                val updatedAwards = safeAwards + newAward

                // Обновляем список в базе данных
                updateAwardsListUseCase(userId, updatedAwards)

                // Обновляем StateFlow
                _listAwards.value = updatedAwards
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fitchUser() { // TODO: Надо доработать чтобы получать пользователя по id
        viewModelScope.launch {
            try {
                val user = getUserUseCase()
                _users.value = user // user может быть null

                user?.let {
                    _seasonalEventPoints.value = it.seasonalEventPoints
                    _listAwards.value = it.awards
                }

            } catch (e: Exception) {
                _users.value = null // TODO: Это нам зачем?
                e.printStackTrace()
            }
        }
    }

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