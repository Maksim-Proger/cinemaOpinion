package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.introductoryscreens.ui.onboarding.OnBoardingScreen
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.MainScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.EditPersonalInformationScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.SettingsScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModel.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.introduction.OnBoardingViewModel

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

        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    event = {
                        viewModel.onEvent(it)
                    }
                )
            }
        }

        composable(
            Route.MainScreen.route
        ) {
            MainScreen(navController)
        }

        composable(Route.EditPersonalInformationScreen.route) {
            EditPersonalInformationScreen(
                stringResource(R.string.edit_personal_information), // TODO: подумать как иначе передать стрингу
                navController
            )
        }

        composable(
            Route.SettingsScreen.route
        ) {
            SettingsScreen(themeViewModel, navController)
        }

    }
}