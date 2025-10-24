package com.example.core.utils.state

sealed class LoadingState {
    data object Loading : LoadingState()
    data object Success : LoadingState()
    data object Error : LoadingState()
}