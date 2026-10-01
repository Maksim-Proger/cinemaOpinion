package com.example.ui.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.lerp

private val AuroraPalettes = listOf(
    // Синяя
    listOf(
        Color(0xFF050A1E),
        Color(0xFF102B7A),
        Color(0xFF2B5BE0),
        Color(0xFF0B1C4D),
        Color(0xFF081536)
    ),
    // Бирюзовая
    listOf(
        Color(0xFF04141C),
        Color(0xFF0A4A5E),
        Color(0xFF17A3B8),
        Color(0xFF083544),
        Color(0xFF061E2A)
    ),
    // Зелёная
    listOf(
        Color(0xFF07180C),
        Color(0xFF12633A),
        Color(0xFF3DD68C),
        Color(0xFF0A3D24),
        Color(0xFF0A2B14)
    )
)

private val BaseBg = Color(0xFF070B14)

@Composable
fun AuroraGradientOverlay(
    active: Boolean,
    modifier: Modifier = Modifier,
    palettes: List<List<Color>> = AuroraPalettes,
    driftDurationMillis: Int = 10_000,
    counterDriftDurationMillis: Int = 16_000,
    wobbleDurationMillis: Int = 7_000,
    paletteStepMillis: Int = 5_000,
    fadeDurationMillis: Int = 900,
    baseColor: Color = BaseBg
) {
    // Плавное появление/исчезновение всего слоя
    val alpha by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = tween(durationMillis = fadeDurationMillis),
        label = "alpha"
    )

    // Пока слой полностью прозрачен — не рисуем и не гоняем анимации (экономим кадры)
    if (alpha <= 0f) return

    val infinite = rememberInfiniteTransition(label = "aurora")

    // Дрейф основного слоя: за период полосы сдвигаются ровно на период
    // зеркального повторения градиента, поэтому рестарт анимации бесшовный
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = driftDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )

    // Встречный дрейф второго слоя (движется в противоположную сторону)
    val counterDrift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = counterDriftDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "counterDrift"
    )

    // «Дыхание» ширины полос: внутренние стопы градиента медленно гуляют
    val wobble by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = wobbleDurationMillis, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble"
    )

    // Непрерывный бег по кругу палитр: 0..palettes.size, дробная часть —
    // степень перехода от текущей гаммы к следующей
    val paletteShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = palettes.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = paletteStepMillis * palettes.size,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "paletteShift"
    )

    Box(
        modifier = modifier.drawBehind {
            val w = size.width
            val h = size.height

            // Текущая гамма — плавная интерполяция между соседними палитрами
            val idx = paletteShift.toInt()
            val from = palettes[idx % palettes.size]
            val to = palettes[(idx + 1) % palettes.size]
            val t = paletteShift - idx
            val colors = List(from.size) { k -> lerp(from[k], to[k], t) }

            // Неравномерные позиции стопов + «дыхание»: полосы широкие и неровные.
            // Коэффициенты у sway разные, чтобы стопы не пересекались.
            val sway = (wobble - 0.5f) * 0.16f
            val stops = arrayOf(
                0f to colors[0],
                (0.26f + sway) to colors[1],
                (0.50f - sway * 0.6f) to colors[2],
                (0.74f + sway * 0.8f) to colors[3],
                1f to colors[4]
            )

            // Слой 1: основные полосы, плывут слева направо.
            // band — «длина» одного прохода палитры (широкие полосы),
            // сдвиг за цикл равен 2*band — периоду зеркального повторения.
            val band = w * 1.35f
            val shift1 = drift * band * 2f
            drawRect(
                brush = Brush.linearGradient(
                    *stops,
                    start = Offset(shift1, 0f),
                    end = Offset(shift1 + band, h * 0.18f),   // лёгкий наклон полос
                    tileMode = TileMode.Mirror
                ),
                alpha = alpha
            )

            // Слой 2: шире, прозрачнее, плывёт навстречу с обратным наклоном —
            // интерференция двух движений даёт «живую» неровность
            val band2 = w * 1.9f
            val shift2 = -counterDrift * band2 * 2f
            drawRect(
                brush = Brush.linearGradient(
                    colors = colors.asReversed(),
                    start = Offset(shift2, h * 0.12f),
                    end = Offset(shift2 + band2, -h * 0.06f),
                    tileMode = TileMode.Mirror
                ),
                alpha = 0.4f * alpha
            )

            // Затухание к низу: сверху свечение остаётся, внизу — базовый фон
            drawRect(
                brush = Brush.verticalGradient(
                    0f to Color.Transparent,
                    0.30f to baseColor.copy(alpha = 0.35f),
                    0.68f to baseColor.copy(alpha = 0.92f),
                    1f to baseColor
                )
            )
        }
    )
}

