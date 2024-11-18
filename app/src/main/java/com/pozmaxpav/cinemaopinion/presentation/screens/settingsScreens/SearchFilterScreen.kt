package com.pozmaxpav.cinemaopinion.presentation.screens.settingsScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.CompositeRequest
import com.pozmaxpav.cinemaopinion.presentation.components.ClassicTopAppBar
import com.pozmaxpav.cinemaopinion.utilits.parsYearsToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterScreen(
    onSearch: (CompositeRequest) -> Unit,
    onSendRequest: () -> Unit,
    onClickClose: () -> Unit,
) {

    // region Переменные
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showGenresList by remember { mutableStateOf(false) }
    var showCountriesList by remember { mutableStateOf(false) }

    // Переменные для хранения выбранной страны и жанра
    var selectedCountry by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var selectedGenre by remember { mutableStateOf<Pair<Int, String>?>(null) }

    // Переменные для хранения выбранного типа
    var selectedType by remember { mutableStateOf("") }

    // Переменная для хранения слова поиска
    var searchWordResult by remember { mutableStateOf("") }

    // Переменные для хранения выбранного диапазона возможных годов
    var selectedRange by remember { mutableStateOf(1900f..2024f) } // Диапазон значений
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    // endregion

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ClassicTopAppBar(
                title = stringResource(R.string.title_for_advanced_search_screen),
                scrollBehavior = scrollBehavior,
                onTransitionAction = onClickClose
            )
        }
    ) { innerPadding ->

        if (showGenresList) {
            ShowListGenres { genre ->
                selectedGenre = genre
                showGenresList = false
            }
            BackHandler {
                showGenresList = !showGenresList
            }
        }
        if (showCountriesList) {
            ShowListCountries { country ->
                selectedCountry = country
                showCountriesList = false
            }
            BackHandler{
                showCountriesList = !showCountriesList
            }
        }

        Column(
            Modifier.padding(innerPadding)
        ) {

            SelectType(
                onSelectedType = { type ->
                    selectedType = type
                }
            )

            SearchKeyword(
                searchWordResult = { newSearchWord ->
                    searchWordResult = newSearchWord
                }
            )

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

            Spacer(modifier = Modifier.padding(7.dp))

            SliderRatingFrom(
                sliderPosition = sliderPosition,
                sliderPositionResult = { newPosition ->
                    sliderPosition = newPosition
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
                                selectedType,
                                searchWordResult,
                                selectedCountry?.first,
                                selectedGenre?.first,
                                sliderPosition.toInt(),
                                parsYearsToString(selectedRange)[0].toInt(),
                                parsYearsToString(selectedRange)[1].toInt(),
                            )
                        )
                        onClickClose()
                        onSendRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
fun SelectType(
    onSelectedType: (String) -> Unit
) {
    val fixWidth = 150.dp
    var selected1 by remember { mutableStateOf(false) }
    var selected2 by remember { mutableStateOf(false) }
    var selected3 by remember { mutableStateOf(false) }
    var selected4 by remember { mutableStateOf(false) }
    var selected5 by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                FilterChip(
                    modifier = Modifier
                        .width(fixWidth),
//                    colors = FilterChipDefaults.filterChipColors(
//                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                    ),
                    selected = selected1,
                    onClick = {
                        selected1 = !selected1
                        onSelectedType("FILM")
                    },
                    label = { Text(text = "Фильм") },
                    leadingIcon = if (selected1) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
                FilterChip(
                    modifier = Modifier
                        .width(fixWidth),
                    selected = selected2,
                    onClick = {
                        selected2 = !selected2
                        onSelectedType("TV_SHOW")
                    },
                    label = { Text(text = "Передача") },
                    leadingIcon = if (selected2) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                FilterChip(
                    modifier = Modifier
                        .width(fixWidth),
                    selected = selected3,
                    onClick = {
                        selected3 = !selected3
                        onSelectedType("TV_SERIES")
                    },
                    label = { Text(text = "Сериал") },
                    leadingIcon = if (selected3) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )

                FilterChip(
                    modifier = Modifier
                        .width(fixWidth),
                    selected = selected4,
                    onClick = {
                        selected4 = !selected4
                        onSelectedType("MINI_SERIES")
                    },
                    label = { Text(text = "Мини сериал") },
                    leadingIcon = if (selected4) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null
                )
            }
        }
    }

    FilterChip(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 49.dp),
        selected = selected5,
        onClick = {
            selected5 = !selected5
            onSelectedType("ALL")
        },
        label = { Text(text = "Показать все") },
        leadingIcon = if (selected5) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else null
    )
}

@Composable
fun SearchKeyword(
    searchWordResult: (String) -> Unit,
) {
    val (searchWord, setSearchWord) = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
            )
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = searchWord,
            onValueChange = setSearchWord,
            label = {
                Text(
                    text = "Введите название фильма",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            trailingIcon = if (searchWord.isNotEmpty()) {
                {
                    IconButton(onClick = { setSearchWord("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.description_clear_text),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            } else null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    searchWordResult(searchWord)
                    keyboardController?.hide()
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
        )
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
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 70.dp)
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
                    headlineContent = {
                        Text(
                            text = country.second
                        )
                    }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 70.dp)
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
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        RangeSlider(
            modifier = Modifier.padding(horizontal = 10.dp),
            value = range,
            onValueChange = onRangeChange,
            valueRange = 1900f..2024f,
            steps = 124,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTickColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

@Composable
fun SliderRatingFrom(
    sliderPosition: Float,
    sliderPositionResult: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Text(
            text = "Укажите минимальный рейтинг: $sliderPosition",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Slider(
            modifier = Modifier.padding(horizontal = 10.dp),
            valueRange = 0f .. 10f,
            value = sliderPosition,
            onValueChange = sliderPositionResult,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTickColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
    }
}

