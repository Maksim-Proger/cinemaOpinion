package com.pozmaxpav.cinemaopinion.utilits.state

sealed class State(
    val isLoading: Boolean = false
) {
    data object Loading : State(isLoading = true)
    data object Success : State()
}

