package com.pozmaxpav.cinemaopinion.utilits.state

sealed class State {
    data object Loading : State()
    data object Success : State()
    data object Error : State()
}

