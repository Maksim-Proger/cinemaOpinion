package com.pozmaxpav.cinemaopinion.presentation.screens.auxiliaryScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.ui.theme.CinemaOpinionTheme
import com.pozmaxpav.cinemaopinion.utilits.TextField
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement

@Composable
fun EditPersonalInformationScreen() {

    var (username, setUsername) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = setUsername,
            label = { stringResource(id = R.string.drop_down_menu_item_edit) },
            placeholder = { stringResource(id = R.string.placeholder_for_edit_name_field) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_edit_change_name)
                )
            },
            supportingText = { stringResource(id = R.string.text_for_edit_name_field) },
            keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
                onDone = {
                    // Здесь можно выполнить какое-то действие при нажатии клавиши Enter.
                    // Например, закрыть клавиатуру.
                    keyboardController?.hide()
                }
            )
        )

        TextField(
            value = username,
            onValueChange = setUsername,
            label = { stringResource(id = R.string.text_for_edit_email_field) },
            placeholder = { stringResource(id = R.string.placeholder_for_edit_email_field) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_edit_change_email)
                )
            },
            supportingText = { stringResource(id = R.string.placeholder_for_edit_email_field) },
            keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
                onDone = {
                    // Здесь можно выполнить какое-то действие при нажатии клавиши Enter.
                    // Например, закрыть клавиатуру.
                    keyboardController?.hide()
                }
            )
        )

        TextField(
            value = username,
            onValueChange = setUsername,
            label = { stringResource(id = R.string.text_for_edit_password_field) },
            placeholder = { stringResource(id = R.string.placeholder_for_edit_password_field) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_edit_change_password)
                )
            },
            supportingText = { stringResource(id = R.string.placeholder_for_edit_password_field) },
            keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
                onDone = {
                    // Здесь можно выполнить какое-то действие при нажатии клавиши Enter.
                    // Например, закрыть клавиатуру.
                    keyboardController?.hide()
                }
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EditPersonalInformationScreenPreview() {
    CinemaOpinionTheme {
        EditPersonalInformationScreen()
    }
}