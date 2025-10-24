package com.pozmaxpav.cinemaopinion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.auth.presentation.navigation.authNavGraph
import com.example.intro.presentation.navigation.introNavGraph
import com.example.ui.presentation.viewmodels.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.MediaNewsScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.SeriesControlScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main.ScaffoldMainScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListOfChangesScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListSelectedGeneralMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListSelectedGeneralSerials
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListSelectedMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListSharedScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListWaitingContinuationSeries
import com.pozmaxpav.cinemaopinion.presentation.screens.screenslists.ListWatchedMovies
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens.EditPersonalInformationScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens.MovieDetailScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens.SettingsScreen
import com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens.WebViewScreen
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@Composable
fun NavGraph(
    themeViewModel: ThemeViewModel,
    systemViewModel: SystemViewModel,
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.MainScreen.route) {
            ScaffoldMainScreen(navController, systemViewModel)
        }
        composable(Route.MediaNewsScreen.route) { MediaNewsScreen(navController) }
        composable(Route.ListOfChangesScreen.route) {
            ListOfChangesScreen(navController, systemViewModel)
        }
        composable(Route.EditPersonalInformationScreen.route) {
            EditPersonalInformationScreen(navController, systemViewModel)
        }
        composable(Route.SeriesControlScreen.route) {
            SeriesControlScreen(navController, systemViewModel)
        }
        composable(Route.SettingsScreen.route) {
            SettingsScreen(themeViewModel, navController)
        }
        composable(Route.ListWatchedMovies.route) {
            ListWatchedMovies(navController, systemViewModel)
        }
        composable(Route.ListSelectedGeneralMovies.route) {
            ListSelectedGeneralMovies(navController, systemViewModel)
        }
        composable(Route.ListSelectedGeneralSerials.route) {
            ListSelectedGeneralSerials(navController, systemViewModel)
        }
        composable(Route.ListSelectedMovies.route) {
            ListSelectedMovies(navController, systemViewModel)
        }
        composable(Route.ListWaitingContinuationSeries.route) {
            ListWaitingContinuationSeries(navController, systemViewModel)
        }
        composable(
            Route.ListSharedScreen.route,
            arguments = listOf(
                navArgument("listId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val listId = backStackEntry.arguments?.getString("listId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            ListSharedScreen(
                navController = navController,
                systemViewModel = systemViewModel,
                listId = listId,
                title = title
            )
        }

        composable( // TODO: В Route это заносить не нужно?
            "webView/{url}", // Шаблон маршрута с аргументом "url"
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) {
            WebViewScreen()
        }

        composable(
            Route.MovieDetailScreen.route,
            arguments = listOf(
                navArgument("newDataSource") { type = NavType.StringType },
                navArgument("movieId") { type = NavType.IntType },
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val newDataSource = backStackEntry.arguments?.getString("newDataSource") ?: ""
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            MovieDetailScreen(
                navController = navController,
                newDataSource = newDataSource,
                movieId = movieId,
                userName = userName
            )
        }

        authNavGraph(
            onLoginSuccess = { userId ->
                systemViewModel.saveUserId(userId)
                navigateFunction(navController, Route.MainScreen.route)
            }
        )

        introNavGraph()

    }
}

