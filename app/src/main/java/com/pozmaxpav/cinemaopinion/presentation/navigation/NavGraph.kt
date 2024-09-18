package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.MainScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.EditPersonalInformationScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.SettingsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModel.ThemeViewModel

@Composable
fun NavGraph(
    themeViewModel: ThemeViewModel,
    startDestination: String
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.MainScreen.route) { MainScreen(navController) }
        composable(Route.EditPersonalInformationScreen.route) { EditPersonalInformationScreen(navController) }
        composable(Route.SettingsScreen.route) { SettingsScreen(themeViewModel, navController) }
    }
}