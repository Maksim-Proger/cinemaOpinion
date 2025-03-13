//package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FabPosition
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.res.stringResource
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.pozmaxpav.cinemaopinion.R
//import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
//import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
//import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
//import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
//import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
//import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
//import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
//import com.pozmaxpav.cinemaopinion.utilits.showToast
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditPersonalInformationScreen(
//    nameToast: String,
//    navController: NavHostController,
//    viewModel: UserViewModel = hiltViewModel(),
//    firebaseViewModel: FirebaseViewModel = hiltViewModel()
//) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//
//    LaunchedEffect(Unit) {
//        viewModel.fitchUser()
//    }
//
//    val user by viewModel.users.collectAsState()
//    val (firstName, setFirstName) = remember { mutableStateOf("") }
//    val (lastName, setLastName) = remember { mutableStateOf("") }
//
//    // Выводим данные из базы, если они там есть.
//    // Запускаем LaunchedEffect только для обновления значений, если user не null.
//    LaunchedEffect(user) {
//        user?.let {
//            setFirstName(it.firstName)
//            setLastName(it.lastName)
//        }
//    }
//
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val context = LocalContext.current
//
//    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            ClassicTopAppBar(
//                title = stringResource(id = R.string.top_app_bar_header_name_user_information),
//                scrollBehavior = scrollBehavior,
//                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
//            )
//        },
//        floatingActionButton = {
//            FabButton(
//                imageIcon = Icons.Default.Add,
//                contentDescription = stringResource(id = R.string.description_floating_action_button_save),
//                textFloatingButton = stringResource(id = R.string.floating_action_button_save),
//                onButtonClick = {
//                    val newUser = DomainUser(
//                        user?.id!!,
//                        firstName,
//                        lastName
//                    )
//                    viewModel.updateUser(newUser)
//                    firebaseViewModel.updatingUserData(newUser)
//                    navigateFunction(navController, Route.MainScreen.route)
//                    showToast(context, nameToast)
//                },
//                expanded = true
//            )
//        },
//        floatingActionButtonPosition = FabPosition.End
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            CustomTextField(
//                value = firstName,
//                onValueChange = setFirstName,
//                label = { Text(stringResource(id = R.string.text_for_edit_name_field)) },
//                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = stringResource(id = R.string.description_icon_edit_change_name),
//                        tint = MaterialTheme.colorScheme.outline
//                    )
//                },
//                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_name_field)) },
//                keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
//                    onDone = {
//                        // Переназначение действия клавиши Enter.
//                        keyboardController?.hide() // При нажатии закрываем клавиатуру.
//                    }
//                )
//            )
//
//            CustomTextField(
//                value = lastName,
//                onValueChange = setLastName,
//                label = { Text(stringResource(id = R.string.text_for_edit_lastName_field)) },
//                placeholder = { Text(stringResource(id = R.string.placeholder_for_edit_lastName_field)) },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = stringResource(id = R.string.description_icon_edit_change_lastname),
//                        tint = MaterialTheme.colorScheme.outline
//                    )
//                },
//                supportingText = { Text(stringResource(id = R.string.placeholder_for_edit_lastName_field)) },
//                keyboardActions = KeyboardActions( // Обрабатываем нажатие клавиши Enter.
//                    onDone = {
//                        // Переназначение действия клавиши Enter.
//                        keyboardController?.hide() // При нажатии закрываем клавиатуру.
//                    }
//                )
//            )
//        }
//    }
//}