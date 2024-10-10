package com.example.introductoryscreens.ui.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.introductoryscreens.R
import com.example.introductoryscreens.ui.onboarding.Page
import com.example.introductoryscreens.ui.onboarding.pages
import com.example.introductoryscreens.ui.theme.TextColor
import com.example.introductoryscreens.util.Dimens.MediumPadding2

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    page: Page
) {
    val introductionScreensFontFamily = FontFamily(Font(R.font.bad_script))

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
                style = TextStyle(
                    fontFamily = introductionScreensFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 35.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp
                ),
                color = TextColor
            )

            Text(
                text = page.description,
                modifier = Modifier.padding(horizontal = MediumPadding2),
                style = TextStyle(
                    fontFamily = introductionScreensFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp
                ),
                color = TextColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFun() {
    OnBoardingPage(
        page = pages[0]
    )
}
