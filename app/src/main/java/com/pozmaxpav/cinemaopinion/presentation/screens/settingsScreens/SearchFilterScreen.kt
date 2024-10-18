package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.CompositeRequest
import com.pozmaxpav.cinemaopinion.utilits.parsYearsToString

@Composable
fun SearchFilterScreen(
    onSearch: (CompositeRequest) -> Unit,
    onSendRequest: () -> Unit,
    onClickClose: () -> Unit,
) {

    var showGenresList by remember { mutableStateOf(false) }
    var showCountriesList by remember { mutableStateOf(false) }

    // Переменные для хранения выбранной страны и жанра
    var selectedCountry by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var selectedGenre by remember { mutableStateOf<Pair<Int, String>?>(null) }

    // Переменные для хранения выбранного диапазона возможных годов
    var selectedRange  by remember { mutableStateOf(1900f..2024f) } // Диапазон значений

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
    ) {

        if (showGenresList) {
            ShowListGenres { genre ->
                selectedGenre = genre
                showGenresList = false
            }
        }
        if (showCountriesList) {
            ShowListCountries { country ->
                selectedCountry = country
                showCountriesList = false
            }
        }

        Column(
            Modifier.padding(vertical = 70.dp)
        ) {

            Text(
                text = stringResource(R.string.title_for_advanced_search_screen),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.padding(vertical = 32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showCountriesList = !showCountriesList
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(
                            text = "Страна",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = selectedCountry?.second ?: "Страна",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.padding(7.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            showGenresList = !showGenresList
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(
                            text = "Жанр",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = selectedGenre?.second ?: "Жанр",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            RangeSliderToSelectDate(
                range = selectedRange,
                onRangeChange = { newRange ->
                    selectedRange = newRange  // Обновляем выбранный диапазон
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        onSearch(
                            CompositeRequest(
                                null,
                                selectedCountry?.first,
                                selectedGenre?.first,
                                null,
                                parsYearsToString(selectedRange)[0].toInt(),
                                parsYearsToString(selectedRange)[1].toInt(),
                            )
                        )
                        onClickClose()
                        onSendRequest()
                    }
                ) {
                    Text(
                        text = "Показать",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ShowListCountries(onCountrySelected: (Pair<Int, String>) -> Unit) {
    val listCountries = listOf(
        Pair(1, "США"),
        Pair(3, "Франция"),
        Pair(5, "Великобритания"),
        Pair(33, "СССР"),
        Pair(34, "Россия")
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 70.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(listCountries) { country ->
                ListItem(
                    modifier = Modifier.clickable {
                        onCountrySelected(country)
                    },
                    headlineContent = { Text(text = country.second) }
                )
            }
        }
    }
}

@Composable
fun ShowListGenres(onGenreSelected: (Pair<Int, String>) -> Unit) {
    val listGenres = listOf(
        Pair(1, "триллер"),
        Pair(2, "драма"),
        Pair(3, "криминал"),
        Pair(4, "мелодрама"),
        Pair(5, "детектив"),
        Pair(6, "фантастика")
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 70.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(listGenres) { genre ->
                ListItem(
                    modifier = Modifier.clickable {
                        onGenreSelected(genre) // Возвращаем выбранную страну
                    },
                    headlineContent = { Text(text = genre.second) }
                )
            }
        }
    }
}

@Composable
fun RangeSliderToSelectDate(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Выберите диапазон: ${range.start.toInt()} - ${range.endInclusive.toInt()}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        RangeSlider(
            modifier = Modifier.padding(horizontal = 10.dp),
            value = range,
            onValueChange = onRangeChange,
            valueRange = 1900f..2024f,
            steps = 124
        )
    }
}



