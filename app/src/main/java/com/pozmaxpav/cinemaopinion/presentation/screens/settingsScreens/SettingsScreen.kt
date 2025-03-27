package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.SettingsRadioButtons
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.FireBaseMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.ThemeViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeViewModel: ThemeViewModel,
    navController: NavHostController
) {
    val viewModelFirebase: FireBaseMovieViewModel = hiltViewModel()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val myStringArray = stringArrayResource(R.array.my_string_array)
    val optionsList = myStringArray.toList()
    var developerMode by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Для режима разработчика
    val (developerComment, setDeveloperComment) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold (
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleId = R.string.drop_down_menu_item_settings,
                scrollBehavior = scrollBehavior,
                onTransitionAction = { navigateFunction(navController, Route.MainScreen.route) }
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

            Spacer(modifier = Modifier.padding(16.dp))
            Button(
                onClick = {
                    developerMode = !developerMode
                }
            ) {
                Text(
                    text = "Открыть режим разработчика"
                )
            }

//            if (developerMode) {
//                Spacer(modifier = Modifier.padding(16.dp))
//                Text(
//                    text = "Технический комментарий",
//                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
//                )
//                TextField(
//                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//                    value = developerComment,
//                    onValueChange = setDeveloperComment,
//                    trailingIcon = if (developerComment.isNotEmpty()) {
//                        {
//                            IconButton(onClick = { setDeveloperComment("") }) {
//                                Icon(
//                                    imageVector = Icons.Default.Close,
//                                    contentDescription = stringResource(id = R.string.description_clear_text),
//                                    tint = MaterialTheme.colorScheme.onPrimary
//                                )
//                            }
//                        }
//                    } else null,
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions(
//                        onDone = {
//                            viewModelFirebase.savingChangeRecord(
//                                context,
//                                "Разработчик",
//                                if (developerComment.isEmpty()) "добавил важный комментарий: $DEVELOPER_COMMENT"
//                                else "добавил важный комментарий: $developerComment"
//                            )
//                            keyboardController?.hide()
//                        }
//                    ),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
//                    )
//                )
//            }
        }
    }
}

