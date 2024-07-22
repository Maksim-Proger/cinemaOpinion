package com.pozmaxpav.cinemaopinion.presentation.navigation

sealed class Route(val route: String) {
    object MainScreen : Route(route = "main_screen")
    object EditPersonalInformationScreen : Route(route = "edit_personal_information_screen")
    object SettingsScreen : Route(route = "settings_screen")
}