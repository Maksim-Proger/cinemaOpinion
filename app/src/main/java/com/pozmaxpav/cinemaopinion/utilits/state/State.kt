package com.pozmaxpav.cinemaopinion.utilits.state

sealed class State {
    data object Loading : State()
    data object Success : State()
    data object Error : State()
}

sealed class StateDuplicate {
    data object Success : StateDuplicate()
    data object Error : StateDuplicate()
}

