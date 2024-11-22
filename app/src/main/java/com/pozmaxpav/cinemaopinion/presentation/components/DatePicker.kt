package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun DatePickerFunction(
    onCloseDatePicker: () -> Unit,
    onDateSelected: (Pair<Int, String>) -> Unit
) {
    var selectedMonth by remember { mutableIntStateOf(1) }
    var selectedYear by remember { mutableIntStateOf(2024) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 45.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Card (
            modifier = Modifier.fillMaxHeight(0.3f),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // TODO: Переработать стили
                        text = "Месяц",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(

                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(12) { month ->
                            Text(
                                text = String.format(
                                    Locale.getDefault(),
                                    "%02d",
                                    month + 1
                                ),
                                modifier = Modifier
                                    .width(50.dp)
                                    .clickable {
                                        selectedMonth = month + 1
                                    }
                                    .background(
                                        if (selectedMonth == month + 1) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent
                                    ),
                                color = if (selectedMonth == month + 1) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text( // TODO: Переработать стили
                        text = "Год",
                        style = MaterialTheme.typography.displayMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        items((0..124).toList().reversed()) { index ->
                            val year = 1900 + index
                            Text(
                                text = year.toString(),
                                modifier = Modifier
                                    .width(60.dp)
                                    .clickable {
                                        selectedYear = year
                                    }
                                    .background(
                                        if (selectedYear == year) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent
                                    ),
                                color = if (selectedYear == year) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Card(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            onDateSelected(
                                Pair(
                                    selectedYear, String.format(
                                        Locale.getDefault(),
                                        "%02d",
                                        selectedMonth
                                    )
                                )
                            )
                            onCloseDatePicker()
                        }
                    ),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Подтвердить",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
