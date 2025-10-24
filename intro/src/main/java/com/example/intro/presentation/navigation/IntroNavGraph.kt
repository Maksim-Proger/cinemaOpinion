package com.example.intro.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.intro.presentation.introscreens.screens.OnBoardingScreen

fun NavGraphBuilder.introNavGraph() {
    composable(IntroRoute.ON_BOARDING_SCREEN) {
        OnBoardingScreen()
    }
}

