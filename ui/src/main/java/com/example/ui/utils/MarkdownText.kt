package com.example.ui.utils

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val context = LocalContext.current
    val textColor = if (color != Color.Unspecified) color.toArgb() else null

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextView(ctx).apply {
                textSize = 16f
                setLineSpacing(0f, 1.2f)
                textColor?.let { setTextColor(it) }
            }
        },
        update = { textView ->
            textColor?.let { textView.setTextColor(it) }
            val markwon = Markwon.builder(context)
                .usePlugin(StrikethroughPlugin.create())
                .build()
            markwon.setMarkdown(textView, markdown)
        }
    )
}
