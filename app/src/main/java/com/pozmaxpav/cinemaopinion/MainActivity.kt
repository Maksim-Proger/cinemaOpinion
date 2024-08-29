package com.pozmaxpav.cinemaopinion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pozmaxpav.cinemaopinion.presentation.navigation.NavGraph
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.theme.CinemaOpinionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Настройка окна для использования всего экрана, включая области системы (такие, как статус-бар).
         * Это позволяет содержимому окна отображаться за пределами системных областей.
         */
        WindowCompat.setDecorFitsSystemWindows(window, false) // TODO: доп разбор

        setContent {
            CinemaOpinionTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(startDestination = Route.MainScreen.route)
                }
            }
        }
    }
}

