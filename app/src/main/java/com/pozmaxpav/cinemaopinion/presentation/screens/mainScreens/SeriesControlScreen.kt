package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.showToast

@Composable
fun SeriesControlScreen(
    onClickCloseButton: () -> Unit,
    viewModel: SeriesControlViewModel = hiltViewModel()
) {

    val listMovies by viewModel.listMovies.collectAsState()
    var openBottomSheet by remember { mutableStateOf(false) }
    val (titleMovie, setTitleMovie) = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (openBottomSheet) {
        MyBottomSheet(
            onClose = {
                openBottomSheet = false
            },
            content = {
                CustomTextField(
                    value = titleMovie,
                    onValueChange = setTitleMovie,
                    placeholder = { Text("Введите название фильма/сериала") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    },
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.insertMovies(titleMovie)
                            showToast(context, "Элемент добавлен")
                        }
                    )
                )
            },
            fraction = 0.3f
        )
        BackHandler {
            openBottomSheet = false
        }
    }

    Scaffold(
        topBar = {

        },
        floatingActionButton = {
            FabButton(
                imageIcon = Icons.Default.Add,
                contentDescription = "Кнопка добавить",
                textFloatingButton = "Добавить",
                onButtonClick = { openBottomSheet = true },
                expanded = true
            )

        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(listMovies, key = {it.id}) { movie ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${movie.title} - ${movie.season} сезон ${movie.series} серия",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

