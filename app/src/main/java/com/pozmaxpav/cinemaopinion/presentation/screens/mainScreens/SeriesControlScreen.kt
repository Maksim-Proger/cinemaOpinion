package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.SeriesControlModel
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SeriesControlScreen(
    onClickCloseButton: () -> Unit,
    viewModel: SeriesControlViewModel = hiltViewModel()
) {
    var selectedNote by remember { mutableStateOf<SeriesControlModel?>(null) }
    val listMovies by viewModel.listMovies.collectAsState()
    var openBottomSheet by remember { mutableStateOf(false) }
    var openBottomSheet2 by remember { mutableStateOf(false) }
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
                            viewModel.insertMovie(titleMovie)
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
        modifier = Modifier.padding(vertical = 25.dp),
        topBar = {
            IconButton(
                onClick = onClickCloseButton
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null
                )
            }
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

        if (openBottomSheet2) {
            MyBottomSheet(
                onClose = { openBottomSheet2 = false },
                content = {
                    ChangeItem(movie = selectedNote!!) {
                        openBottomSheet2 = false
                    }
                },
                fraction = 0.5f
            )
            BackHandler {
                openBottomSheet2 = false
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(listMovies, key = { it.id }) { movie ->

                var isVisible by remember { mutableStateOf(true) }
                AnimatedVisibility(
                    visible = isVisible,
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it }, // Уходит влево
                        animationSpec = tween(durationMillis = 300) // Длительность анимации
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Card(
                            modifier = Modifier
                                .weight(0.9f)
                                .wrapContentHeight(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Item(movie) {
                                selectedNote = movie
                                openBottomSheet2 = true
                            }
                        }

                        Spacer(modifier = Modifier.padding(horizontal = 10.dp))

                        IconButton(
                            modifier = Modifier
                                .weight(0.1f),
                            onClick = {
                                isVisible = false // Скрываем элемент перед удалением
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(300)
                                    viewModel.deleteMovie(movie.id)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }
        }
    }
}

@Composable
private fun Item(
    movie: SeriesControlModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = "${movie.title} - ${movie.season} сезон ${movie.series} серия",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeItem(
    movie: SeriesControlModel,
    viewModel: SeriesControlViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val (season, setSeason) = remember { mutableStateOf("") }
    val (series, setSeries) = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        setSeason(movie.season.toString())
        setSeries(movie.series.toString())
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = season,
        onValueChange = setSeason,
        placeholder = { Text("Укажите сезон") },
        trailingIcon =  if (season.isNotEmpty()) {
            {
                IconButton(onClick = { setSeason("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.description_clear_text),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(
            // Указываем какой тип клавиатуры будет использоваться.
            keyboardType = KeyboardType.Number,
            // Указываем, каким образом будет обрабатываться нажатие клавиши Enter.
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline
        )
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = series,
        onValueChange = setSeries,
        placeholder = { Text("Укажите серию") },
        trailingIcon =  if (series.isNotEmpty()) {
            {
                IconButton(onClick = { setSeries("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.description_clear_text),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else null,
        keyboardOptions = KeyboardOptions(
            // Указываем какой тип клавиатуры будет использоваться.
            keyboardType = KeyboardType.Number,
            // Указываем, каким образом будет обрабатываться нажатие клавиши Enter.
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline
        )
    )

    Button(
        onClick = {
            viewModel.updateMovie(movie.id, season.toInt(), series.toInt())
            onClick()
        }
    ) {
        Text(text = "Сохранить")
    }
}

