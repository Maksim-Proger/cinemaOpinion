package com.pozmaxpav.cinemaopinion.presentation.screens.mainScreens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.presentation.navigation.Route
import com.pozmaxpav.cinemaopinion.presentation.theme.CommentAddedColor
import com.pozmaxpav.cinemaopinion.presentation.theme.FilmAddedColor
import com.pozmaxpav.cinemaopinion.presentation.theme.FilmDeleteColor
import com.pozmaxpav.cinemaopinion.presentation.theme.SpecialHorizontalDividerColor
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.utilits.navigateFunction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfChangesScreen(
    navController: NavHostController,
    viewModel: FirebaseViewModel = hiltViewModel()
) {
    val list by viewModel.listOfChanges.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecordsOfChanges()
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Последние изменения",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navigateFunction(navController, Route.MainScreen.route)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = stringResource(id = R.string.description_icon_home_button),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
    ) { innerPadding ->
        if (list.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(10.dp)
            ) {
                itemsIndexed(list.reversed()) { index, it ->
                    var color = colorMethod(it.noteText)
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "Изменения от: ",
                                color = color
                            )
                            Text(
                                text =
                                SimpleDateFormat(
                                    "dd.MM.yyyy",
                                    Locale.getDefault()
                                ).format(
                                    Date(it.timestamp)
                                ),
                                color = color
                            )
                        }
                        Text(
                            text = "${it.username} ${it.noteText}",
                            color = color
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 10.dp),
                            color = SpecialHorizontalDividerColor
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ничего не произошло..."
                )
            }
        }
    }
}

fun colorMethod(text: String) : Color  { // TODO: Изменить цвета на нормальные
    return when {
        text.contains("добавил(а) комментарий к фильму") -> CommentAddedColor
        text.contains("удалил(а) фильм") -> FilmDeleteColor
        text.contains("добавил(а) фильм") -> FilmAddedColor
        else -> Color.Transparent
    }
}

fun instructionsForColors() {
    // TODO: Написать инструкцию по цветам
}