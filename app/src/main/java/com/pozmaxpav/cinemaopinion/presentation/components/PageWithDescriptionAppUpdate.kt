package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pozmaxpav.cinemaopinion.utilits.Constants.NEW_YEAR_TEXT

@Composable
fun PageDescription(
    onDismiss: () -> Unit
) {

    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Spacer(Modifier.padding(innerPadding))
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .heightIn(75.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "\u2744",
                    fontSize = 65.sp
                )

                Text(
                    text = "\u2744",
                    fontSize = 65.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 7.dp)
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = NEW_YEAR_TEXT,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Card (
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "С праздником!",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .heightIn(75.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "\u2744",
                    fontSize = 65.sp
                )

                Text(
                    text = "\u2744",
                    fontSize = 65.sp
                )
            }
        }
    }
}
