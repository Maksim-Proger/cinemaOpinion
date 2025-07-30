package com.pozmaxpav.cinemaopinion.presentation.funs

import com.pozmaxpav.cinemaopinion.domain.usecase.system.ClearUserDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogoutManager @Inject constructor(
    private val clearUserDataUseCase: ClearUserDataUseCase
){
    suspend fun logout(clearLoginFlag: () -> Unit) {
        withContext(Dispatchers.IO) {
            clearUserDataUseCase()
        }
        clearLoginFlag()
    }
}