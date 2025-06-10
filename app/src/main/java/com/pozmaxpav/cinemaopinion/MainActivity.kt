package com.pozmaxpav.cinemaopinion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.NavGraph
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.theme.CinemaOpinionTheme
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.introduction.IntroductionScreensViewModel
import com.pozmaxpav.cinemaopinion.utilits.CheckAndUpdateAppVersion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val mainViewModel: MainViewModel = hiltViewModel()
            val introductionScreensViewModel: IntroductionScreensViewModel = hiltViewModel()
            val startDestination by introductionScreensViewModel.startDestination.collectAsState()
            val isLoading by introductionScreensViewModel.isLoading.collectAsState()
            val registrationFlag by mainViewModel.registrationFlag.collectAsState()
            val context = LocalContext.current

            // Получаем "destination" из ярлыка
            val destinationFromShortcut = intent.extras?.getString("destination")
            val destination = destinationFromShortcut ?: if (registrationFlag) startDestination else Route.LoginScreen.route

            CinemaOpinionTheme(themeViewModel = themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (!isLoading) {
                        CheckAndUpdateAppVersion(context)
                        NavGraph(
                            themeViewModel = themeViewModel,
                            startDestination = destination
                        )
                    }
                }
            }
        }
    }
}

