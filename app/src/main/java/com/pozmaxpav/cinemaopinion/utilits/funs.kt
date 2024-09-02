package com.pozmaxpav.cinemaopinion.utilits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.Country
import com.pozmaxpav.cinemaopinion.domain.models.Genre
import com.pozmaxpav.cinemaopinion.domain.models.MovieData
import java.time.DateTimeException
import java.time.Month

@Composable
fun AccountListItem(icon: Painter, contentDescription: String, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable { /*TODO: onClick*/ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon =  if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.description_clear_text)
                    )
                }
            }
        } else null,
        supportingText = supportingText,
        keyboardOptions = KeyboardOptions(
            // Указываем какой тип клавиатуры будет использоваться.
            keyboardType = KeyboardType.Text,
            // Указываем, каким образом будет обрабатываться нажатие клавиши Enter.
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        singleLine = singleLine,
    )
}


@Composable
fun WorkerWithImage(
    movie: MovieData,
    height: Dp
) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = movie.nameRu ?: stringResource(id = R.string.no_movie_title),
        modifier = Modifier.height(height),
        contentScale = ContentScale.Fit
    )
}


@Composable
fun DetailsCardFilm(
    movie: MovieData,
    onClick: () -> Unit,
    padding: PaddingValues
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(padding)
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onClick() }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = null)
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WorkerWithImage(movie, 250.dp)
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = movie.nameRu ?: stringResource(id = R.string.no_movie_title),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCountries(movie.countries),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    when(movie) {
                        is MovieData.Movie -> {
                            Text(
                                text = movie.premiereRu,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatGenres(movie.genres),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        is MovieData.MovieTop -> {
                            Text(
                                text = movie.year,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = movie.rating,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        is MovieData.MovieSearch -> {}
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "Add to my list")
                }
                Button(
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "Add to general list")
                }
            }
        }
    }
}

fun formatGenres(genres: List<Genre>): String {
    return genres.joinToString(separator = ", ") {
        it.genre
    }
}

fun formatCountries(country: List<Country>): String {
    return country.joinToString(separator = ", ") {
        it.country
    }
}


// TODO: доработать метод
fun formatMonth(fullDate: String): String {
    val listDate: List<String> = fullDate.split("-")
    return try {
        Month.of(listDate[1].toInt()).name.capitalize()
    } catch (e: DateTimeException) {
        "Invalid month"
    }
}

// TODO: доработать метод
fun formatYear(fullDate: String) : Int {
    val listDate: List<String> = fullDate.split("-")
    return listDate[0].toInt()
}