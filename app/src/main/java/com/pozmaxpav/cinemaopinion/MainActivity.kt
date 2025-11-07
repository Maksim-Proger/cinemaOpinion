package com.pozmaxpav.cinemaopinion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.auth.presentation.navigation.AuthRoute
import com.example.intro.presentation.introscreens.viewmodel.OnBoardingViewModel
import com.example.intro.presentation.navigation.IntroRoute
import com.example.ui.presentation.theme.AppTheme
import com.example.ui.presentation.viewmodels.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.NavGraph
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.CheckAndUpdateAppVersion
import com.pozmaxpav.cinemaopinion.utilits.LoadingScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            // TODO: Переработать, чтобы уменьшить уровень привязки. Убрать мерцание.
            val onBoardingViewModel: OnBoardingViewModel = hiltViewModel()
            val hasEntered by onBoardingViewModel.hasUserEnteredApp.collectAsState(initial = null)

            val themeViewModel: ThemeViewModel = hiltViewModel()
            val systemViewModel: SystemViewModel = hiltViewModel()

            val registrationFlag by systemViewModel.registrationFlag.collectAsState()

            // Получаем "destination" из ярлыка
            val destinationFromShortcut = intent.extras?.getString("destination")
            val destination = destinationFromShortcut
                ?: if (registrationFlag) Route.MainScreen.route else AuthRoute.LOGIN_SCREEN

            AppTheme(themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when(hasEntered) {
                        true -> {
                            CheckAndUpdateAppVersion(context)
                            NavGraph(
                                themeViewModel = themeViewModel,
                                systemViewModel = systemViewModel,
                                startDestination = destination
                            )
                        }
                        false -> {
                            NavGraph(
                                themeViewModel = themeViewModel,
                                systemViewModel = systemViewModel,
                                startDestination = IntroRoute.ON_BOARDING_SCREEN
                            )
                        }
                        null -> LoadingScreen()
                    }
                }
            }
        }
    }
}

