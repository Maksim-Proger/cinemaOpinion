package com.pozmaxpav.cinemaopinion.utilits

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.SelectedMovie
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.Country
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.Genre
import com.pozmaxpav.cinemaopinion.domain.models.moviemodels.MovieData
import java.time.DateTimeException
import java.time.Month

@Composable
fun AccountListItem(
    icon: Painter,
    contentDescription: String,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 15.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        shape = RoundedCornerShape(16.dp),
        onValueChange = onValueChange,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline
        ),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon =  if (value.isNotEmpty()) {
            {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.description_clear_text),
                        tint = MaterialTheme.colorScheme.onPrimary
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
fun SelectedItem(
    movie: SelectedMovie,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        WorkerWithImageSelectedMovie(
            movie = movie,
            height = 90.dp
        )
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = movie.nameFilm,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// TODO: Доработать
@Composable
fun ShowSelectedMovie(
    movie: SelectedMovie,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            WorkerWithImageSelectedMovie(
                movie = movie,
                height = 90.dp
            )
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Text(
                text = movie.nameFilm,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
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
fun WorkerWithImageSelectedMovie(
    movie: SelectedMovie,
    height: Dp
) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = movie.nameFilm,
        modifier = Modifier.height(height),
        contentScale = ContentScale.Fit
    )
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

// Кастуем объект MovieData в SelectedMovie
fun MovieData.toSelectedMovie(): SelectedMovie {
    return when (this) {
        is MovieData.Movie -> SelectedMovie(
            id = this.kinopoiskId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )
        is MovieData.MovieTop -> SelectedMovie(
            id = this.filmId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )
        is MovieData.MovieSearch -> SelectedMovie(
            id = this.kinopoiskId,
            nameFilm = this.nameRu?: "Нет названия",
            posterUrl = this.posterUrl
        )
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun returnToMainScreen(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// TODO: Подумать над методом еще
fun parsYearsToString(range:ClosedFloatingPointRange<Float>): List<String> {
    val yearsList = listOf<String>(
        range.start.toInt().toString(),
        range.endInclusive.toInt().toString()
    )
    return yearsList
}