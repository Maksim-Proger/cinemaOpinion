package com.pozmaxpav.cinemaopinion.utilits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.pozmaxpav.cinemaopinion.domain.models.MovieData

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
        Text(text = title)
    }
}


@Composable
fun TextField(
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
        contentDescription = movie.nameRu,
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
            .fillMaxSize()
            .padding(padding)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    WorkerWithImage(movie, 250.dp)

                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = movie.nameRu ?: "Нет названия",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = movie.year ?: "Нет года выпуска",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = movie.countries.toString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        when(movie) {
                            is MovieData.Movie -> {
                                Text(
                                    text = movie.genres.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            is MovieData.MovieTop -> {
                                Text(
                                    text = movie.rating.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            is MovieData.MovieSearch -> {}
                        }
                    }
                }
            }
        }
    }
}
