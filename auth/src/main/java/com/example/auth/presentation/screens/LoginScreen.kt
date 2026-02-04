package com.example.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.auth.R
import com.example.auth.presentation.viewmodel.AuthViewModel
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.text.CustomTextField
import com.example.ui.presentation.components.topappbar.TopAppBarAllScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String, Boolean) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val userData by authViewModel.userData.collectAsState()
    val loginResult by authViewModel.authResult.collectAsState()

    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    var openBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.isSuccess) {
                userData?.let { user ->
                    onLoginSuccess(user.id, true)
                }
            }
            authViewModel.resetAuthResult()
        }
    }
    LaunchedEffect(Unit) {
        authViewModel.toastMessage.collect { resId ->
            val message = context.getString(resId)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarAllScreens(
                context,
                titleId = R.string.title_login_screen,
                scrollBehavior = scrollBehavior,
                onShowTransitionAction = false
            )
        }
    ) { innerPadding ->

        if (openBottomSheet) {
            CustomBottomSheet(
                onCloseRequest = { openBottomSheet = false }
            ) { onClose ->
                RegistrationScreen(
                    authViewModel = authViewModel,
                    keyboardController = keyboardController,
                    focusManager = focusManager,
                    fraction = 0.9f,
                    onClose = onClose
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // region Email
            CustomTextField(
                value = login,
                onValueChange = setLogin,
                label = {
                    Text(
                        stringResource(id = R.string.text_for_email_field),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource
                            (id = R.string.description_icon_add_name),
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
            // endregion

            Spacer(Modifier.padding(10.dp))

            // region Password
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
                        contentDescription = stringResource
                            (id = R.string.description_icon_add_name),
                        tint = MaterialTheme.colorScheme.outline
                    )
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
            // endregion

            Spacer(Modifier.padding(26.dp))

            // region Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextButton(
                    textButton = stringResource(R.string.button_registration),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier,
                    onClickButton = { openBottomSheet = true }
                )
                CustomTextButton(
                    textButton = stringResource(R.string.button_login),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier,
                    onClickButton = {
                        authViewModel.authorization(login, password)
                    }
                )
            }
            // endregion
        }
    }
}

