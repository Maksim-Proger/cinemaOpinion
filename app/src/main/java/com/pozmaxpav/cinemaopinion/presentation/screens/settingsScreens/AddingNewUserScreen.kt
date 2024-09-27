package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField

@Composable
fun AddingNewUserScreen(
    onClick: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {

    val (firstName, setFirstName) = remember { mutableStateOf("") }
    val (lastName, setLastName) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        floatingActionButton = {
            FabButton(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
                textFloatingButton = stringResource(id = R.string.floating_action_button_save),
                onButtonClick = {
                    // Сохранение пользователя в базе данных
                    viewModel.addUser(firstName, lastName)
                    // После сохранения можно сбросить поля ввода
                    setFirstName("")
                    setLastName("")
                    onClick()
                },
                expanded = true
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
            // TODO: Переделать текстовые надписи
            CustomTextField(
                value = firstName,
                onValueChange = setFirstName,
                label = { Text(stringResource(id = R.string.text_for_edit_name_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )

            CustomTextField(
                value = lastName,
                onValueChange = setLastName,
                label = { Text(stringResource(id = R.string.text_for_edit_name_field)) },
                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}