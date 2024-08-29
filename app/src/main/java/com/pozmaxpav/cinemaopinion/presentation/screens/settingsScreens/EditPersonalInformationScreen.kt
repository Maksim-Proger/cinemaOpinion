package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPersonalInformationScreen(navController: NavHostController) {

    val (username, setUsername) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.top_app_bar_header_name_user_information)) },
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
                            contentDescription = stringResource(id = R.string.description_icon_home_button)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FabButton(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                textFloatingButton = stringResource(id = R.string.floating_action_button_save),
                onButtonClick = { /* TODO */ }
            )
        },
        floatingActionButtonPosition = FabPosition.End

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CustomTextField(
                value = username,
                onValueChange = setUsername,
                label = { Text(stringResource(id = R.string.text_for_edit_name_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name)
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
                    onDone = {
                        // Переназначение действия клавиши Enter.
                        keyboardController?.hide() // При нажатии закрываем клавиатуру.
                    }
                )
            )

            CustomTextField(
                value = email,
                onValueChange = setEmail,
                label = { Text(stringResource(id = R.string.text_for_edit_email_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_email_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_email)
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_email_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )

            CustomTextField(
                value = password,
                onValueChange = setPassword,
                label = { Text(stringResource(id = R.string.text_for_edit_password_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_password_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_password)
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_password_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}