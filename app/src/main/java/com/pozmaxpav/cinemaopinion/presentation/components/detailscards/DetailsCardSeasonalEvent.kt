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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.ratingbar.RatingBarScaffold
import com.pozmaxpav.cinemaopinion.presentation.viewModel.api.ApiViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.AuxiliaryUserViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.system.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.firebase.PersonalMovieViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.showToast

@Composable
fun DetailsCard(
    newYearMovie: DomainSelectedMovieModel,
    onCloseButton: () -> Unit,
    padding: PaddingValues,
    mainViewModel: MainViewModel = hiltViewModel(),
    auxiliaryUserViewModel: AuxiliaryUserViewModel = hiltViewModel(),
    selectedMovieViewModel: PersonalMovieViewModel = hiltViewModel(),
    apiViewModel: ApiViewModel = hiltViewModel()
) {

    val userId by mainViewModel.userId.collectAsState()
    val info by apiViewModel.informationMovie.collectAsState()
    var showRatingBar by remember { mutableStateOf(false) }
    val quantitySeasonalEventPoints by auxiliaryUserViewModel.seasonalEventPoints.collectAsState()
//    val statusExist by selectedMovieViewModel.status.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(userId) {
        auxiliaryUserViewModel.getUserData(userId)
        auxiliaryUserViewModel.getSeasonalEventPoints(userId)
    }
    LaunchedEffect(newYearMovie.id) {
        apiViewModel.getInformationMovie(newYearMovie.id)
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
                        title = stringResource(R.string.text_for_expandedCard_field),
                        description = info?.description ?: stringResource(R.string.limit_is_over)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        // region кнопка "Посмотрел"
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
                            Text(
                                text = stringResource(R.string.button_viewed),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        // endregion

                        // region кнопка "Добавить к себе"
//                        Button(
//                            onClick = {
//                                selectedMovieViewModel.addMovieToPersonalList(userId, newYearMovie)
//                                if (statusExist == "error") showToast(context, R.string.movie_has_already_been_added)
//                                else showToast(context, R.string.movie_has_been_added)
//                            },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = MaterialTheme.colorScheme.secondary,
//                                contentColor = MaterialTheme.colorScheme.onSecondary
//                            ),
//                        ) {
//                            Text(
//                                text = stringResource(R.string.button_add_to_yourself),
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
                        // endregion

                    }
                }
            }
        }
    }
}

