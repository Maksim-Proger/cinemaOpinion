package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.introductoryscreens.ui.onboarding.OnBoardingScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListOfChangesScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListSelectedGeneralMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListSelectedGeneralSerials
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListSelectedMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListWaitingContinuationSeries
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.ListWatchedMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.LoginScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.MainScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.MediaNewsScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens.SeriesControlScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.EditPersonalInformationScreen
//import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.EditPersonalInformationScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.SettingsScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens.WebViewScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.introduction.OnBoardingViewModel

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

        composable(Route.MediaNewsScreen.route) {
            MediaNewsScreen(navController)
        }

        composable(Route.ListOfChangesScreen.route) {
            ListOfChangesScreen(navController)
        }

        composable(Route.EditPersonalInformationScreen.route) {
            EditPersonalInformationScreen(navController)
        }

        composable(Route.SeriesControlScreen.route) {
            SeriesControlScreen(navController)
        }

        composable(Route.SettingsScreen.route) {
            SettingsScreen(themeViewModel, navController)
        }

        composable( // TODO: В Route это заносить не нужно?
            "webView/{url}", // Шаблон маршрута с аргументом "url"
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) {
            WebViewScreen()
        }

        composable(Route.ListWatchedMovies.route) {
            ListWatchedMovies(navController)
        }

        composable(Route.ListSelectedGeneralMovies.route) {
            ListSelectedGeneralMovies(navController)
        }

        composable(Route.ListSelectedGeneralSerials.route) {
            ListSelectedGeneralSerials(navController)
        }

        composable(Route.ListSelectedMovies.route) {
            ListSelectedMovies(navController)
        }

        composable(Route.ListWaitingContinuationSeries.route) {
            ListWaitingContinuationSeries(navController)
        }

        composable(Route.LoginScreen.route) {
            LoginScreen(navController)
        }
    }
}