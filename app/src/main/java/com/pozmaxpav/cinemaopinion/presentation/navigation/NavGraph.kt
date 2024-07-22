package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pozmaxpav.cinemaopinion.presentation.screens.MainScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.auxiliaryScreens.EditPersonalInformationScreen

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.MainScreen.route) { MainScreen(navController) }
        composable(Route.EditPersonalInformationScreen.route) { EditPersonalInformationScreen() }
    }
}