package com.example.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.auth.presentation.viewmodel.AuthViewModel
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.text.CustomTextField

@Composable
fun RegistrationScreen(
    authViewModel: AuthViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    fraction: Float,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    var triggerOnClick by remember { mutableStateOf(false) }

    LaunchedEffect(triggerOnClick) {
        if (triggerOnClick) {
            authViewModel.toastMessage.collect { resId ->
                val message = context.getString(resId)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onClose()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            value = nikName,
            onValueChange = setNikName,
            label = {
                Text(
                    stringResource(id = R.string.text_for_name_field),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_add_name),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.padding(10.dp))
        CustomTextField(
            value = email,
            onValueChange = setEmail,
            label = {
                Text(
                    stringResource(id = R.string.text_for_email_field),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_add_name),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            ),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.padding(10.dp))
        CustomTextField(
            value = password,
            onValueChange = setPassword,
            label = {
                Text(
                    stringResource(id = R.string.text_for_password_field),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.description_icon_add_name),
                    tint = MaterialTheme.colorScheme.outline
                )
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            ),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.padding(26.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextButton(
                textButton = stringResource(R.string.button_save),
                modifier = Modifier,
                onClickButton = {
                    authViewModel.addUser(nikName, email, password)
                    triggerOnClick = true
                }
            )
        }
    }
}