package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.presentation.components.FabButton
import com.pozmaxpav.cinemaopinion.presentation.components.MyBottomSheet
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.utilits.CustomTextField
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesControlScreen(
    navController: NavHostController,
    seriesControlViewModel: SeriesControlViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val listMovies by seriesControlViewModel.listMovies.collectAsState()
    val userId by mainViewModel.userId.collectAsState()
    var selectedEntry by remember { mutableStateOf<DomainSeriesControlModel?>(null) }
    var openBottomSheetAdd by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(userId) {
        seriesControlViewModel.getListEntries(userId)
        seriesControlViewModel.observeListEntries(userId)
    }

    if (openBottomSheetAdd) {
        MyBottomSheet(
            onClose = { openBottomSheetAdd = false },
            content = {
                AddItem(
                    seriesControlViewModel,
                    userId
                ) {
                    openBottomSheetAdd = false
                }
            },
            fraction = 0.3f
        )
        BackHandler {
            openBottomSheetAdd = false
        }
    }

    if (openBottomSheetChange) {
        MyBottomSheet(
            onClose = { openBottomSheetChange = false },
            content = {
                ChangeItem(
                    userId,
                    selectedEntry!!,
                    seriesControlViewModel
                ) {
                    openBottomSheetChange = false
                }
            },
            fraction = 0.5f
        )
        BackHandler {
            openBottomSheetChange = false
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleId = R.string.title_series_control_screen,
                scrollBehavior = scrollBehavior,
                onTransitionAction = {
                    navigateFunction(navController, Route.MainScreen.route)
                }
            )
        },
        floatingActionButton = {
            FabButton(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(R.string.content_description_for_button_add),
                textFloatingButton = stringResource(R.string.button_add),
                onButtonClick = { openBottomSheetAdd = true },
                expanded = true
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(listMovies, key = { it.id }) { entry ->
                var isVisible by remember { mutableStateOf(true) }
                AnimatedVisibility(
                    visible = isVisible,
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(0.9f)
                                ) {
                                    Item(entry) {
                                        selectedEntry = entry
                                        openBottomSheetChange = true
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        isVisible = false
                                        CoroutineScope(Dispatchers.Main).launch {
                                            delay(300)
                                            seriesControlViewModel.deleteMovie(userId, entry.id)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }
            item { Spacer(Modifier.padding(45.dp)) }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            seriesControlViewModel.onCleared()
        }
    }
}

@Composable
private fun Item(
    movie: DomainSeriesControlModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Text(
            text = "${movie.title} - ${movie.season} сезон ${movie.series} серия",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun AddItem(
    seriesControlViewModel: SeriesControlViewModel,
    userId: String,
    onClickCloseButton: () -> Unit
) {

    val (titleMovie, setTitleMovie) = remember { mutableStateOf("") }
    val context = LocalContext.current

    CustomTextField(
        value = titleMovie,
        onValueChange = setTitleMovie,
        placeholder = {
            Text(
                stringResource(R.string.enter_the_name_of_the_movie_series),
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                seriesControlViewModel.addNewEntry(userId, titleMovie)
                showToast(context, R.string.element_has_been_added)
                setTitleMovie("")
                onClickCloseButton()
            }
        )
    )
}

@Composable
private fun ChangeItem(
    userId: String,
    selectedEntry: DomainSeriesControlModel,
    seriesControlViewModel: SeriesControlViewModel,
    onClick: () -> Unit
) {
    val (season, setSeason) = remember { mutableStateOf("") }
    val (series, setSeries) = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        setSeason(selectedEntry.season.toString())
        setSeries(selectedEntry.series.toString())
    }

    // region Season
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = season,
        onValueChange = setSeason,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text("Укажите сезон") },
        trailingIcon = if (season.isNotEmpty()) {
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
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        colors = textFieldColors()
    )
    // endregion

    // region series
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = series,
        onValueChange = setSeries,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text("Укажите серию") },
        trailingIcon = if (series.isNotEmpty()) {
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
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        colors = textFieldColors()
    )
    // endregion

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 15.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                seriesControlViewModel.updateMovie(
                    userId,
                    selectedEntry.id,
                    selectedEntry.title,
                    season.toInt(),
                    series.toInt()
                )
                onClick()
            }
        ) { Text(text = "Сохранить") }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun textFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
    focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
    unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
    focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
    unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline
)

