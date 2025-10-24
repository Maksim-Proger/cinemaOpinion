package com.example.ui.utils

fun capitalizeSentences(text: String): String {
    if (text.isEmpty()) return text
    val sentences = text.split("(?<=[.!?]\\s)|(?<=[.!?]\$)".toRegex())
    return sentences.joinToString("") { sentence ->
        if (sentence.isBlank()) sentence
        else {
            sentence.trimStart().replaceFirstChar { firstChar ->
                if (firstChar.isLowerCase()) firstChar.titlecase() else firstChar.toString()
            }
        }
    }
}