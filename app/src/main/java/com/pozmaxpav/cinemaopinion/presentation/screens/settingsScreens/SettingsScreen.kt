package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {

    val myStringArray = stringArrayResource(R.array.my_string_array)
    val optionsList = myStringArray.toList()

    val languagesArray = stringArrayResource(R.array.array_of_languages)
    val languagesList = languagesArray.toList()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.drop_down_menu_item_settings),
                        style = MaterialTheme.typography.displayLarge,
                        color = colorResource(R.color.color_text_header_top_app_bar)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Route.MainScreen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.description_icon_home_button),
                            tint = colorResource(R.color.color_icon_button_top_app_bar)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_background_top_app_bar)
                )
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            ThemeApplication(optionsList)
            HorizontalDivider()
            ThemeApplication(languagesList)
        }
    }
}


@Composable
fun ThemeApplication(optionsList: List<String>) {
    var selectedOption by remember { mutableStateOf(optionsList[0]) }

    Card(
        modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.color_account_card),
            contentColor = colorResource(id = R.color.color_text_account_card)
        )
    ) {
        Column(
            modifier = Modifier.wrapContentHeight().fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            optionsList.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option }
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = null
                    )

                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}