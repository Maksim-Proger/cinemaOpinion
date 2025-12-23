package com.pozmaxpav.cinemaopinion.presentation.screens.settingsscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_MOVIES
import com.example.core.utils.CoreDatabaseConstants.NODE_LIST_SERIALS
import com.pozmaxpav.cinemaopinion.R
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens
import com.example.ui.presentation.components.SettingsRadioButtons
import com.example.ui.presentation.viewmodels.ThemeViewModel
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SharedListsViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.navigateFunctionClearAllScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userName: String,
    themeViewModel: ThemeViewModel,
    navController: NavHostController,
    viewModel: SharedListsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    val myStringArray = stringArrayResource(R.array.my_string_array)
    val optionsList = myStringArray.toList()

    val indexSelectedTheme by themeViewModel.indexSelectedTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarAllScreens(
                context = context,
                titleId = R.string.drop_down_menu_item_settings,
                scrollBehavior = scrollBehavior,
                onTransitionAction = {
                    navigateFunctionClearAllScreens(navController, Route.MainScreen.route)
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 7.dp))
            Card(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                SettingsRadioButtons(
                    indexSelectedTheme = indexSelectedTheme,
                    optionsList = optionsList
                ) { option ->
                    when (option) {
                        optionsList[0] -> { // Логика для Темной темы
                            themeViewModel.saveIndexTheme(0)
                            themeViewModel.changeModeTheme(true)
                            themeViewModel.changeStatusUsingSystemTheme(false)
                        }

                        optionsList[1] -> { // Логика для Светлой темы
                            themeViewModel.saveIndexTheme(1)
                            themeViewModel.changeModeTheme(false)
                            themeViewModel.changeStatusUsingSystemTheme(false)
                        }

                        optionsList[2] -> { // Логика для Системной темы
                            themeViewModel.saveIndexTheme(2)
                            themeViewModel.changeStatusUsingSystemTheme(true)
                        }
                    }
                }
            }

            if (userName == "Разработчик") {
                Spacer(modifier = Modifier.padding(vertical = 7.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        TextButton(
                            onClick = {}
                        ) {
                            Text(
                                text = "Добавить фильм",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(vertical = 7.dp))
                    Card(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.sendMovies(
                                    sourceNode = NODE_LIST_SERIALS,
                                    listId = "a8116a1d-85a3-4bf6-8b6a-aa551fd1b100"
                                )
                            }
                        ) {
                            Text(
                                text = "Перенести фильмы",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

