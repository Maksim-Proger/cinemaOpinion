package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsRadioButtons
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.ThemeViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    navController: NavHostController
) {
    val myStringArray = stringArrayResource(R.array.my_string_array)
    val optionsList = myStringArray.toList()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.drop_down_menu_item_settings),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.description_icon_home_button),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            SettingsRadioButtons(optionsList) { option ->
                when (option) {
                    optionsList[0] -> {
                        // Логика для Темной темы
                        themeViewModel.changeModeTheme(true)
                        themeViewModel.changeStatusUsingSystemTheme(false)
                    }
                    optionsList[1] -> {
                        // Логика для Светлой темы
                        themeViewModel.changeModeTheme(false)
                        themeViewModel.changeStatusUsingSystemTheme(false)
                    }
                    optionsList[2] -> {
                        // Логика для Системной темы
                        themeViewModel.changeStatusUsingSystemTheme(true)
                    }
                }
            }
        }
    }
}

