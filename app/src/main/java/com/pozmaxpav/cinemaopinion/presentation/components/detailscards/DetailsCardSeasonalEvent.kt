package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.domain.models.firebase.models.SelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.ratingbar.RatingBarScaffold
import com.pozmaxpav.cinemaopinion.presentation.viewModel.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
//import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.showToast

@Composable
fun DetailsCard(
    newYearMovie: SelectedMovieModel,
    onCloseButton: () -> Unit,
    padding: PaddingValues,
    addToPersonalList: String,
    errorToast: String,
    mainViewModel: MainViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    selectedMovieViewModel: SelectedMovieViewModel = hiltViewModel(),
) {

    val userId by mainViewModel.userId.collectAsState()
    val info by mainViewModel.informationMovie.collectAsState()
    var showRatingBar by remember { mutableStateOf(false) }
    val quantitySeasonalEventPoints by auxiliaryUserViewModel.seasonalEventPoints.collectAsState()
    val statusExist by selectedMovieViewModel.status.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
        auxiliaryUserViewModel.getSeasonalEventPoints(userId)
    }

    LaunchedEffect(newYearMovie.id) {
        mainViewModel.getInformationMovie(newYearMovie.id.toInt())
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(padding)
    ) {

        if (showRatingBar) {
            RatingBarScaffold(
                score = quantitySeasonalEventPoints,
                onCloseButton = {
                    showRatingBar = !showRatingBar
                }
            )
            BackHandler {
                showRatingBar = false
            }
        }

        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onCloseButton() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
                    .verticalScroll(scrollState)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    WorkerWithImageSelectedMovie(
                        movie = newYearMovie,
                        height = 200.dp
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 7.dp),
                ) {
                    Text(
                        text = "Название фильма: ${newYearMovie.nameFilm}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.padding(15.dp))

                    ExpandedCard(
                        title = "Описание",
                        description = info?.description ?: "К сожалению, суточный лимит закончился"
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        // region watched
                        Button(
                            onClick = {
                                auxiliaryUserViewModel.updatingEventData(userId)
                                showRatingBar = !showRatingBar
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ) {
                            Text(text = "Я посмотрел",)
                        }
                        // endregion

                        // region ButtonAddToPersonalList
                        Button(
                            onClick = {
                                selectedMovieViewModel.addMovieToPersonalList(userId, newYearMovie)
                                if (statusExist == "error") {
                                    showToast(context, errorToast)
                                } else showToast(context, addToPersonalList)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ) {
                            Text(
                                text = "Добавить к себе"
                            )
                        }
                        // endregion

                    }
                }
            }
        }
    }
}

