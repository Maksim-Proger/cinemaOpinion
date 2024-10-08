package com.pozmaxpav.cinemaopinion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.NavGraph
import com.pozmaxpav.cinemaopinion.presentation.theme.CinemaOpinionTheme
import com.pozmaxpav.cinemaopinion.presentation.viewModel.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.introduction.IntroductionScreensViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Настройка окна для использования всего экрана, включая области системы (такие, как статус-бар).
         * Это позволяет содержимому окна отображаться за пределами системных областей.
         */
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val introductionScreensViewModel: IntroductionScreensViewModel = hiltViewModel()
            val startDestination by introductionScreensViewModel.startDestination.collectAsState()
            val isLoading by introductionScreensViewModel.isLoading.collectAsState()

            CinemaOpinionTheme(themeViewModel = themeViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isLoading) { // Проверяем, завершилась ли загрузка
                        NavGraph(
                            themeViewModel = themeViewModel,
                            startDestination = startDestination
                        )
                    } else {
                        // TODO: Можно добавить экран загрузки или просто оставить пустое пространство
                    }
                }
            }
        }
    }
}

