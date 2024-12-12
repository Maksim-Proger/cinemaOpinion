package com.pozmaxpav.cinemaopinion.utilits.state

sealed class State {
    object Loading : State()
    object Success : State()
//    data class Error(val message: String) : State()
}

