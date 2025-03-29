package com.pozmaxpav.cinemaopinion.utilits

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Country
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.Genre
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

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

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = value,
        shape = RoundedCornerShape(16.dp),
        onValueChange = onValueChange,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.onPrimary
        ),
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = if (value.isNotEmpty()) {
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
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = singleLine,
    )
}

@Composable
fun SelectedMovieItem(
    movie: DomainSelectedMovieModel,
    onClick: () -> Unit,
    showTopBar: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
                showTopBar()
            }
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

fun capitalizeSentences(text: String): String {
    if (text.isEmpty()) return text
    val sentences = text.split("(?<=[.!?]\\s)|(?<=[.!?]\$)".toRegex())
    return sentences.joinToString("") { sentence ->
        if (sentence.isBlank()) sentence
        else {
            sentence.trimStart().replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase() else firstChar.toString()
            }
        }
    }
}

@Composable
fun CustomTextFieldForComments(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp),
        value = value,
        shape = RoundedCornerShape(16.dp),
        onValueChange = { newText ->
            if (newText.endsWith('.') || newText.endsWith('!') || newText.endsWith('?')) {
                onValueChange(capitalizeSentences(newText))
            } else {
                onValueChange(newText)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            focusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
            focusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.onPrimary
        ),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = if (value.isNotEmpty()) {
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
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
fun WorkerWithImageSelectedMovie(
    movie: DomainSelectedMovieModel,
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

fun formatMonth(month: String): String {
    return try {
        Month.of(month.toInt())
            .name
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
    } catch (e: DateTimeException) {
        "Invalid month"
    } catch (e: NumberFormatException) {
        "Invalid month"
    }
}

fun formatDate(date: String): String {
    val formatterInput = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val dateTime = LocalDateTime.parse(date, formatterInput)
    return dateTime.format(formatterOutput)
}

fun deletingOldRecords(timestamp: Long): Boolean {
    val currentTimeMillis = System.currentTimeMillis() // Получаем текущую дату
    val differenceInMillis =
        abs(currentTimeMillis - timestamp) // Рассчитываем разницу между текущим временем и входящей датой
    val daysDifference = differenceInMillis / (1000 * 60 * 60 * 24) // Конвертируем разницу в дни
    return daysDifference > 7
}

// Кастуем объект MovieData в SelectedMovie
fun MovieData.toSelectedMovie(): DomainSelectedMovieModel {
    return when (this) {
        is MovieData.Movie -> DomainSelectedMovieModel(
            id = this.kinopoiskId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )

        is MovieData.MovieTop -> DomainSelectedMovieModel(
            id = this.filmId,
            nameFilm = this.nameRu,
            posterUrl = this.posterUrl
        )

        is MovieData.MovieSearch -> DomainSelectedMovieModel(
            id = this.kinopoiskId,
            nameFilm = this.nameRu ?: "Нет названия",
            posterUrl = this.posterUrl
        )

        is MovieData.MovieSearch2 -> DomainSelectedMovieModel(
            id = this.filmId,
            nameFilm = this.nameRu ?: "Нет названия",
            posterUrl = this.posterUrl
        )
    }
}

fun showToast(context: Context, messageId: Int) {
    val message = context.getString(messageId)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun navigateFunction(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun parsYearsToString(range: ClosedFloatingPointRange<Float>): List<String> {
    val yearsList = listOf(
        range.start.toInt().toString(),
        range.endInclusive.toInt().toString()
    )
    return yearsList
}