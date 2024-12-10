package com.example.introductoryscreens.ui.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.introductoryscreens.ui.onboarding.Page
import com.example.introductoryscreens.ui.theme.TextColor
import com.example.introductoryscreens.util.Dimens.MediumPadding2

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page
) {

    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.85f),
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.padding(vertical = 45.dp))

        Column(
            modifier = Modifier.padding(vertical = 45.dp)
        ) {
            Text(
                text = page.title,
                modifier = Modifier.padding(horizontal = MediumPadding2),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 55.sp,
                    lineHeight = 65.sp,
                ),
                color = TextColor
            )

            Text(
                text = page.description,
                modifier = Modifier.padding(horizontal = MediumPadding2),
                style = MaterialTheme.typography.labelMedium.copy(
                    lineHeight = 34.sp,
                ),
                color = TextColor
            )
        }
    }
}
