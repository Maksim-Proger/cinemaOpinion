package com.pozmaxpav.cinemaopinion.presentation.components.ratingbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pozmaxpav.cinemaopinion.R

@Composable
fun RatingBarScaffold(
    score: Long?,
    onCloseButton: () -> Unit
){
    val maxScore = 80L

    val emotionImage = when(score?.toInt()) {
        null -> R.drawable.default_picture
        in 0..10 -> R.drawable.first
        in 11..20 -> R.drawable.second
        in 21..30 -> R.drawable.third
        in 31..40 -> R.drawable.fourth
        in 41..50 -> R.drawable.fifth
        in 51..60 -> R.drawable.second
        in 61..70 -> R.drawable.seventh
        else -> R.drawable.eighth
    }

    val emotionText = when(score?.toInt()) {
        null -> "Очки: N/A"
        in 0..10 -> "Рождество — время волшебства, когда мы можем поверить в чудеса и делиться добротой с окружающими.\n" +
                "Первый этап пройден. Так держать!"
        in 11..20 -> "Новый год — это возможность начать с чистого листа и воплотить в жизнь самые смелые планы.\n" +
                "А вот и второй за спиной! Не останавливайся."
        in 21..30 -> "Откройте своё сердце для любви и тепла — пусть эти чувства сопровождают вас в новом году.\n" +
                "Хорошо идешь!"
        in 31..40 -> "Новый год напоминает нам о том, что каждый миг — это дар, который стоит ценить и использовать на полную катушку.\n" +
                "А вот и экватор. За это тебе полагается награда! Увидишь ее у себя в личном кабинете!"
        in 41..50 -> "Пусть каждый миг в новом году будет освещен искристым светом надежды и вдохновения.\n" +
                "Вперед!"
        in 51..60 -> "В каждом конце кроется начало, подобно тому, как ночное небо готовит нас к рассвету."
        in 61..70 -> "Стремление к мечтам — это сияющая звезда, которая ведет нас через тьму неопределенности."
        else -> "Ура!!! Вы максимально наполнены духом праздника!\n" +
                "За это тебе полагается награда! Увидишь ее у себя в личном кабинете!"
    }

    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = emotionImage),
                contentDescription = null,
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = emotionText,
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RatingBar(
                score = score,
                maxScore = maxScore,
                numStars = 8,
                starSize = 48.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            val scoreText = if (score != null) {
                "Очки: $score/$maxScore"
            } else {
                "Очки: N/A"
            }
            Text(
                text = scoreText,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onCloseButton
                ) {
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
                                text = "Принято",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}