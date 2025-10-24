package com.example.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.auth.presentation.screens.LoginScreen

fun NavGraphBuilder.authNavGraph(
    onLoginSuccess: (String, Boolean) -> Unit
) {
    composable(AuthRoute.LOGIN_SCREEN) {
        LoginScreen(
            onLoginSuccess = { userId, registrationFlag ->
                onLoginSuccess(userId, registrationFlag)
            }
        )
    }
}