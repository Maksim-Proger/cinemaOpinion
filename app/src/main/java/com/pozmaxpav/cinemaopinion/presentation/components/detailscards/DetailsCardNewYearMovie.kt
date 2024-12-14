package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.ExpandedCard
import com.pozmaxpav.cinemaopinion.presentation.components.ratingbar.RatingBarScaffold
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.SelectedMovieViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel
import com.pozmaxpav.cinemaopinion.utilits.WorkerWithImageSelectedMovie
import com.pozmaxpav.cinemaopinion.utilits.showToast
import com.pozmaxpav.cinemaopinion.utilits.toSelectedMovie

@Composable
fun DetailsCard(
    newYearMovie: SelectedMovie,
    onCloseButton: () -> Unit,
    padding: PaddingValues,
    addToPersonalList: String,
    errorToast: String,
    viewModelFirebase: FirebaseViewModel = hiltViewModel(),
    viewModelMain: MainViewModel = hiltViewModel(),
    viewModelUser: UserViewModel = hiltViewModel(),
    viewModel: SelectedMovieViewModel = hiltViewModel(),
) {
    val user by viewModelUser.users.collectAsState()
    var userId by remember { mutableStateOf("") }
    val info by viewModelMain.informationMovie.collectAsState()
    var showRatingBar by remember { mutableStateOf(false) }
    val getSeasonalEventPoints by viewModelUser.seasonalEventPoints.collectAsState()
    val listAwards by viewModelUser.listAwards.collectAsState()
    val statusExist by viewModel.status.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(newYearMovie.id) {
        viewModelMain.getInformationMovie(newYearMovie.id)
    }

    LaunchedEffect(user) {
        if (user != null) {
            user.let {
                userId = it!!.id.toString()
            }
        }
    }

    LaunchedEffect(listAwards) {
        if (getSeasonalEventPoints == 40L) {
            viewModelFirebase.updateSeasonalEventPoints(
                userId, "awards", listAwards.toString()
            )
        }

        if (getSeasonalEventPoints == 80L) {
            viewModelFirebase.updateSeasonalEventPoints(
                userId, "awards", listAwards.toString()
            )
        }
    }

    LaunchedEffect(getSeasonalEventPoints) {
        viewModelFirebase.updateSeasonalEventPoints(
            userId, "seasonalEventPoints", getSeasonalEventPoints
        )
    }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(padding)
    ) {

        if (showRatingBar) {
            RatingBarScaffold(
                score = getSeasonalEventPoints,
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                viewModelUser.handleEvent(userId)
                                showRatingBar = !showRatingBar
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            ),
                        ) {
                            Text(text = "Я посмотрел",)
                        }
                        Button(
                            onClick = {
                                viewModel.addSelectedMovie(newYearMovie)
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
                    }
                }
            }
        }
    }
}

