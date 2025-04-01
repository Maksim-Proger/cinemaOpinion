package com.pozmaxpav.cinemaopinion.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pozmaxpav.cinemaopinion.R
import com.pozmaxpav.cinemaopinion.utilits.Constants.DEVELOPER_COMMENT
import com.pozmaxpav.cinemaopinion.utilits.Constants.NEW_YEAR_TEXT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageDescription(onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ClassicTopAppBar(
                context = context,
                titleId = R.string.title_page_description,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        WithoutEventText(innerPadding, scrollState, onDismiss)
//        WithEventText(innerPadding, scrollState, onDismiss)
    }
}

@Composable
private fun WithoutEventText(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 25.dp, vertical = 7.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = DEVELOPER_COMMENT,
                style = MaterialTheme.typography.displayMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextButton(
                textButton = stringResource(R.string.button_to_begin),
                textStyle = MaterialTheme.typography.displayLarge,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                onClickButton = onDismiss
            )
        }
    }
}

@Composable
private fun WithEventText(
    innerPadding: PaddingValues,
    scrollState: ScrollState,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Анимация на заднем фоне
        CustomLottieAnimation(
            nameFile = "animation_falling_snow.lottie",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize() // Занимает весь доступный размер
        )

        // Контент на переднем плане
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Прокручиваемый текстовый контент
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 25.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = NEW_YEAR_TEXT,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Кнопка
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Card(
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

            // Украшение
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

