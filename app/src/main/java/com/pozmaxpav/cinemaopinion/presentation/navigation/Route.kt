package com.pozmaxpav.cinemaopinion.presentation.navigation

sealed class Route(val route: String) {
    data object MainScreen : Route(route = "main_screen")
    data object EditPersonalInformationScreen : Route(route = "edit_personal_information_screen")
    data object MediaNewsScreen : Route(route = "media_news_screen")
    data object NotificationsScreen : Route(route = "notifications_screen")
    data object SeriesControlScreen : Route(route = "series_control_screen")
    data object ListSelectedMovies : Route(route = "list_selected_movies")
    data object SettingsScreen : Route(route = "settings_screen/{userName}") {
        fun getUserName(userName: String) = "settings_screen/$userName"
    }

    data object ListSharedScreen :
        Route(route = "list_shared_screen/{listId}/{title}/{userName}") {
        fun getListId(listId: String, title: String, userName: String) =
            "list_shared_screen/$listId/$title/$userName"
    }

    // Экран с параметрами newDataSource (String) и movieId (Int)
    data object MovieDetailScreen :
        Route(route = "movie_details/{listId}/{movieId}/{userName}") {
        // Функция для удобного создания пути с параметром
        fun createRoute(listId: String, movieId: Int, userName: String) =
            "movie_details/$listId/$movieId/$userName"
    }
    data object InternalSharedList :
        Route(route = "internal_shared_list/{dataSource}/{listId}/{title}/{userId}/{userName}") {
        fun openInternalSharedList(
            dataSource: String,
            listId: String,
            title: String,
            userId: String,
            userName: String
        ) = "internal_shared_list/$dataSource/$listId/$title/$userId/$userName"
    }
}

