package com.pozmaxpav.cinemaopinion.utilits.state

sealed class LoadingState {
    data object Loading : LoadingState()
    data object Success : LoadingState()
    data object Error : LoadingState()
}

