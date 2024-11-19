package com.pozmaxpav.cinemaopinion.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _url = MutableStateFlow<String>("")
    val url: StateFlow<String> = _url

    init {
        _url.value = savedStateHandle["url"] ?: ""
    }
}