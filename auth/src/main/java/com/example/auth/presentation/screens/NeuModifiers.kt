package com.example.auth.presentation.screens

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.neuRaised(
    cornerRadius: Dp,
    offset: Dp = 7.dp,
    blur: Dp = 16.dp
) = drawWithCache {
    val cr = cornerRadius.toPx()
    val off = offset.toPx()
    val darkPaint = neuFillPaint(NewColors.NeuDark, blur.toPx())
    val lightPaint = neuFillPaint(NewColors.NeuLight, blur.toPx())
    onDrawBehind {
        drawIntoCanvas { canvas ->
            val nc = canvas.nativeCanvas
            // Тёмная тень — снизу-справа
            nc.drawRoundRect(
                off, off,
                size.width + off, size.height + off,
                cr, cr, darkPaint
            )
            // Светлая тень — сверху-слева
            nc.drawRoundRect(
                -off, -off,
                size.width - off, size.height - off,
                cr, cr, lightPaint
            )
        }
    }
}

internal fun Modifier.neuInset(
    cornerRadius: Dp,
    offset: Dp = 4.dp,
    blur: Dp = 7.dp
) = this
    .background(NewColors.NeuBg, RoundedCornerShape(cornerRadius))
    .drawWithCache {
        val cr = cornerRadius.toPx()
        val off = offset.toPx()
        val darkPaint = neuStrokePaint(NewColors.NeuDark, off * 1.6f, blur.toPx())
        val lightPaint = neuStrokePaint(NewColors.NeuLight, off * 1.6f, blur.toPx())
        val clipPath = Path().apply {
            addRoundRect(0f, 0f, size.width, size.height, cr, cr, Path.Direction.CW)
        }
        onDrawWithContent {
            drawContent()
            drawIntoCanvas { canvas ->
                val nc = canvas.nativeCanvas
                val save = nc.save()
                nc.clipPath(clipPath)
                // Тёмная внутренняя тень — сверху-слева
                nc.drawRoundRect(
                    off, off,
                    size.width + off, size.height + off,
                    cr, cr, darkPaint
                )
                // Светлая внутренняя тень — снизу-справа
                nc.drawRoundRect(
                    -off, -off,
                    size.width - off, size.height - off,
                    cr, cr, lightPaint
                )
                nc.restoreToCount(save)
            }
        }
    }

private fun neuFillPaint(color: Color, blur: Float) = Paint().apply {
    isAntiAlias = true
    this.color = color.toArgb()
    if (blur > 0f) maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)
}

private fun neuStrokePaint(color: Color, strokeWidth: Float, blur: Float) = Paint().apply {
    isAntiAlias = true
    style = Paint.Style.STROKE
    this.strokeWidth = strokeWidth
    this.color = color.toArgb()
    if (blur > 0f) maskFilter = BlurMaskFilter(blur, BlurMaskFilter.Blur.NORMAL)
}
