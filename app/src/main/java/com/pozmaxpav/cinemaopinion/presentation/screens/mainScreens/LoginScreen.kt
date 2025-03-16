package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.CustomTextButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val (login, setLogin) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    var openBottomSheet by remember { mutableStateOf(false) }
    val nameToast = stringResource(R.string.add_new_account)
    val loginErrorToast = stringResource(R.string.login_error)
    val loginCompleted = stringResource(R.string.login_completed)
    val loginVerificationResult by auxiliaryUserViewModel.loginVerificationResult.collectAsState()
    val showToast by auxiliaryUserViewModel.showToast.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(loginVerificationResult) {
        if (loginVerificationResult != null) {
            mainViewModel.saveRegistrationFlag(true)
            mainViewModel.saveUserId(loginVerificationResult!!.id)
            showToast(context, loginCompleted)
            navigateFunction(navController, Route.MainScreen.route)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                title = "Вход / Регистрация",
                scrollBehavior = scrollBehavior,
                onShowTransitionAction = false
            )
        }
    ) { innerPadding ->

        if (openBottomSheet) {
            MyBottomSheet(
                onClose = { openBottomSheet = false },
                content = {
                    RegistrationWindow(
                        auxiliaryUserViewModel,
                        keyboardController,
                        focusManager,
                        onClose = {
                            openBottomSheet = false
                            showToast(context, nameToast)
                        }
                    )
                },
                fraction = 0.9f
            )
        }

        if (showToast) {
            showToast(context, loginErrorToast)
            auxiliaryUserViewModel.resetToastState()
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
                leadingIcon = {
                    androidx.compose.material3.Icon(
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
                leadingIcon = {
                    androidx.compose.material3.Icon(
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
                keyboardType = KeyboardType.Email,
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
                    textButton = "Регистрация",
                    onClickButton = {
                        openBottomSheet = true
                    }
                )
                CustomTextButton(
                    textButton = "Вход",
                    onClickButton = {
                        auxiliaryUserViewModel.checkLoginAndPassword(login, password)
                    }
                )
            }
            // endregion
        }
    }
}

@Composable
fun RegistrationWindow(
    auxiliaryUserViewModel: AuxiliaryUserViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    onClose: () -> Unit
) {

    val (nikName, setNikName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(
            value = nikName,
            onValueChange = setNikName,
            label = { Text(stringResource(id = R.string.text_for_add_name_field)) },
            leadingIcon = {
                androidx.compose.material3.Icon(
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
        Spacer(Modifier.padding(10.dp))
        CustomTextField(
            value = email,
            onValueChange = setEmail,
            leadingIcon = {
                androidx.compose.material3.Icon(
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
        Spacer(Modifier.padding(10.dp))
        CustomTextField(
            value = password,
            onValueChange = setPassword,
            leadingIcon = {
                androidx.compose.material3.Icon(
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
                textButton = "Сохранить",
                paddingEnd = 15.dp,
                onClickButton = {
                    auxiliaryUserViewModel.addUser(nikName, email, password)
                    onClose()
                }
            )
        }
    }
}

