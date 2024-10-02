package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pozmaxpav.cinemaopinion.R
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
        composable(Route.EditPersonalInformationScreen.route) { // TODO: подумать как иначе передать стрингу
            EditPersonalInformationScreen(
                stringResource(R.string.edit_personal_information),
                navController
            )
        }
        composable(Route.SettingsScreen.route) { SettingsScreen(themeViewModel, navController) }
    }
}