package com.pozmaxpav.cinemaopinion.presentation.components.detailscards

import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import kotlin.math.abs

data class DynamicPaletteColors(
    val background: Color,
    val title: Color,
    val accent: Color,
    val buttonBackground: Color
)

@Composable
fun rememberDynamicPaletteColors(
    imageUrl: String?,
    defaultBackground: Color = MaterialTheme.colorScheme.background,
    defaultAccent: Color = MaterialTheme.colorScheme.secondary
): DynamicPaletteColors {
    val context = LocalContext.current

    var dominantColor by remember { mutableStateOf(defaultBackground) }
    var titleColor by remember { mutableStateOf(defaultAccent) }
    var accentColor by remember { mutableStateOf(defaultAccent) }
    var buttonBgColor by remember { mutableStateOf(defaultAccent) }

    LaunchedEffect(imageUrl) {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = context.imageLoader.execute(request)
        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap

        bitmap?.let {
            Palette.from(it).generate { palette ->
                palette?.let { p ->
                    val raw = p.getDominantColor(defaultBackground.toArgb())

                    val hsl = FloatArray(3)
                    ColorUtils.colorToHSL(raw, hsl)
                    val dominantHue = hsl[0]
                    hsl[2] = hsl[2].coerceAtMost(0.2f)
                    dominantColor = Color(ColorUtils.HSLToColor(hsl))

                    titleColor = Color(ColorUtils.HSLToColor(floatArrayOf(dominantHue, 0.2f, 0.9f)))

                    val vibrantSwatch = p.vibrantSwatch
                    val vibrantHsl = FloatArray(3)
                    if (vibrantSwatch != null) {
                        ColorUtils.colorToHSL(vibrantSwatch.rgb, vibrantHsl)
                    }
                    accentColor = if (
                        vibrantSwatch != null &&
                        vibrantHsl[1] > 0.35f &&
                        hueDistance(vibrantHsl[0], dominantHue) <= 40f
                    ) {
                        Color(vibrantSwatch.rgb)
                    } else {
                        Color(ColorUtils.HSLToColor(floatArrayOf(dominantHue, 0.5f, 0.6f)))
                    }

                    buttonBgColor = Color(p.getDarkMutedColor(defaultAccent.toArgb()))
                }
            }
        }
    }

    return DynamicPaletteColors(
        background = animateColorAsState(dominantColor, label = "paletteBackground").value,
        title = animateColorAsState(titleColor, label = "paletteTitle").value,
        accent = animateColorAsState(accentColor, label = "paletteAccent").value,
        buttonBackground = animateColorAsState(buttonBgColor, label = "paletteButtonBackground").value
    )
}

private fun hueDistance(a: Float, b: Float): Float {
    val diff = abs(a - b) % 360f
    return if (diff > 180f) 360f - diff else diff
}
