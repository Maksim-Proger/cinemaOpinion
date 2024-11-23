package com.pozmaxpav.cinemaopinion.presentation.navigation

sealed class Route(
    val route: String
) {
    data object MainScreen : Route(route = "main_screen")
    data object OnBoardingScreen : Route(route = "on_boarding_screen")
    data object AppStartNavigation : Route(route = "app_start_navigation")
    data object EditPersonalInformationScreen : Route(route = "edit_personal_information_screen")
    data object SettingsScreen : Route(route = "settings_screen")
    data object MediaNewsScreen : Route(route = "media_news_screen")
    data object ListOfChangesScreen : Route(route = "list_of_changes_screen")
    data object SeriesControlScreen : Route(route = "series_control_screen")
    data object ListWatchedMovies : Route(route = "list_watched_movies")
}