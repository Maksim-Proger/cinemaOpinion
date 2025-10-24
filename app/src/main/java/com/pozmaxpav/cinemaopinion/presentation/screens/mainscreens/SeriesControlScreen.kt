package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ui.presentation.components.CustomBottomSheet
import com.example.ui.presentation.components.CustomTextButton
import com.example.ui.presentation.components.TopAppBarAllScreens
import com.example.ui.presentation.components.fab.FABMenu
import com.example.ui.presentation.components.text.CustomTextField
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import com.pozmaxpav.cinemaopinion.presentation.components.items.SeriesControlItem
import com.pozmaxpav.cinemaopinion.presentation.components.systemcomponents.OnBackInvokedHandler
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.viewModels.firebase.SeriesControlViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModels.system.SystemViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import com.pozmaxpav.cinemaopinion.utilits.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesControlScreen(
    navController: NavHostController,
    systemViewModel: SystemViewModel,
    seriesControlViewModel: SeriesControlViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val listMovies by seriesControlViewModel.listMovies.collectAsState()
    val userId by systemViewModel.userId.collectAsState()

    var selectedEntry by remember { mutableStateOf<DomainSeriesControlModel?>(null) }
    var openBottomSheetAdd by remember { mutableStateOf(false) }
    var openBottomSheetChange by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        systemViewModel.getUserId()
    }
    LaunchedEffect(userId) {
        seriesControlViewModel.getListEntries(userId)
        seriesControlViewModel.observeListEntries(userId)
    }

    if (openBottomSheetAdd) {
        CustomBottomSheet(
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            OnBackInvokedHandler { openBottomSheetAdd = false }
        } else {
            BackHandler { openBottomSheetAdd = false }
        }
    }

    if (openBottomSheetChange) {
        CustomBottomSheet(
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            OnBackInvokedHandler { openBottomSheetChange = false }
        } else {
            BackHandler { openBottomSheetChange = false }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarAllScreens(
                context = context,
                titleId = R.string.title_series_control_screen,
                scrollBehavior = scrollBehavior,
                onTransitionAction = {
                    navigateFunction(navController, Route.MainScreen.route)
                }
            )
        },
        floatingActionButton = {
            FABMenu(
                imageIcon = Icons.Default.Add,
                contentDescription = stringResource(R.string.content_description_for_button_add),
                onButtonClick = { openBottomSheetAdd = true },
                expanded = false
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(listMovies, key = { it.id }) { entry ->

                var isVisible by remember { mutableStateOf(true) }

                LaunchedEffect(isVisible) {
                    if (!isVisible) {
                        seriesControlViewModel.deleteMovie(userId, entry.id)
                    }
                }

                AnimatedVisibility(
                    visible = isVisible,
                    modifier = Modifier.animateItem(),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(modifier = Modifier.weight(1f)) {
                                SeriesControlItem(entry) {
                                    selectedEntry = entry
                                    openBottomSheetChange = true
                                }
                            }

                            IconButton(
                                onClick = { isVisible = false },
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 10.dp)
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

                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }
            item { Spacer(Modifier.padding(45.dp)) }
        }
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
        label = {
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

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        setSeason(selectedEntry.season.toString())
        setSeries(selectedEntry.series.toString())
    }

    CustomTextField(
        value = season,
        onValueChange = setSeason,
        label = {
            Text(
                text = "Укажите сезон",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    )

    CustomTextField(
        value = series,
        onValueChange = setSeries,
        label = {
            Text(
                text = "Укажите серию",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        CustomTextButton(
            textButton = "Сохранить",
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            endPadding = 15.dp,
            onClickButton = {
                seriesControlViewModel.updateMovie(
                    userId,
                    selectedEntry.id,
                    selectedEntry.title,
                    season.toInt(),
                    series.toInt()
                )
                onClick()
            }
        )
    }
}

