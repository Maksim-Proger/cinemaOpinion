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
    data object ListSelectedGeneralMovies : Route(route = "list_selected_general_movies")
    data object ListSelectedGeneralSerials : Route(route = "list_selected_general_serials")
    data object ListSelectedMovies : Route(route = "list_selected_movies")
    data object ListWaitingContinuationSeries : Route(route = "list_waiting_continuation_series")
    data object LoginScreen : Route(route = "login_screen")

    // Экран с параметрами newDataSource (String) и movieId (Int)
    data object MovieDetailScreen : Route(route = "movie_details/{newDataSource}/{movieId}/{userName}") {
        // Функция для удобного создания пути с параметром
        fun createRoute(newDataSource: String, movieId: Int, userName: String) = "movie_details/$newDataSource/$movieId/$userName"
    }
}

