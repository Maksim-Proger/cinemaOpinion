package com.example.introductoryscreens.ui.onboarding

sealed class OnBoardingEvent {
    data object SaveAppEntry: OnBoardingEvent()
}