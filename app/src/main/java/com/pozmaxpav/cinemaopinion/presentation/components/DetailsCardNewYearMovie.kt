package com.pozmaxpav.cinemaopinion.presentation.components

import android.util.Log
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.domain.models.DomainUser
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.presentation.components.ratingbar.RatingBarScaffold
import com.pozmaxpav.cinemaopinion.presentation.viewModel.FirebaseViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.MainViewModel
import com.pozmaxpav.cinemaopinion.presentation.viewModel.UserViewModel

@Composable
fun DetailsCard(
    newYearMovie: SelectedMovie,
    onCloseButton: () -> Unit,
    padding: PaddingValues,
    viewModelFirebase: FirebaseViewModel = hiltViewModel(),
    viewModelMain: MainViewModel = hiltViewModel(),
    viewModelUser: UserViewModel = hiltViewModel()
) {
    val user by viewModelUser.users.collectAsState()
    var userId by remember { mutableStateOf("") }
    val info by viewModelMain.informationMovie.collectAsState()
    var showRatingBar by remember { mutableStateOf(false) }

    val getSeasonalEventPoints by viewModelUser.seasonalEventPoints.collectAsState()

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

    LaunchedEffect(getSeasonalEventPoints) {
        viewModelFirebase.updateSeasonalEventPoints(
            userId, "seasonalEventPoints", getSeasonalEventPoints // TODO: Доработать запись в базу синхронно с Room
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
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AsyncImage(
                        model = newYearMovie.posterUrl,
                        contentDescription = null,
                        modifier = Modifier.height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Text(
                        text = newYearMovie.nameFilm,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                ExpandedCard(
                    title = "Описание",
                    description = info?.description ?: "К сожалению, суточный лимит закончился"
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { // TODO: Доработать!
                            if (getSeasonalEventPoints < 80) {
                                viewModelUser.incrementSeasonalEventPoints(userId, 10L)
                            }
                            showRatingBar = !showRatingBar
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                    ) {
                        Text(
                            text = "Я посмотрел",
                        )
                    }
                    Button(
                        onClick = {},
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

