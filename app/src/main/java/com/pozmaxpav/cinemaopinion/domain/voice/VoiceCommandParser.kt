package com.pozmaxpav.cinemaopinion.domain.voice

import com.pozmaxpav.cinemaopinion.domain.models.voice.VoiceCommand
import javax.inject.Inject

class VoiceCommandParser @Inject constructor() {

    fun parse(rawText: String): VoiceCommand? {
        val text = normalize(rawText)
        if (text.isEmpty()) return null
        return parseSuffixCommand(text) ?: parseSetExactCommand(text)
    }

    private fun normalize(raw: String): String =
        raw.lowercase()
            .replace('ё', 'е')
            .replace(NOT_LETTER_OR_DIGIT, " ")
            .replace(WHITESPACE, " ")
            .trim()

    private fun parseSuffixCommand(text: String): VoiceCommand? {
        NEW_SEASON_SUFFIXES.forEach { suffix ->
            if (text.endsWith(" $suffix")) {
                return VoiceCommand.NewSeason(text.removeSuffix(" $suffix").trim())
            }
        }
        NEXT_EPISODE_SUFFIXES.forEach { suffix ->
            if (text.endsWith(" $suffix")) {
                return VoiceCommand.NextEpisode(text.removeSuffix(" $suffix").trim())
            }
        }
        return null
    }

    private fun parseSetExactCommand(text: String): VoiceCommand.SetExact? {
        val tokens = text.split(' ')
        val numbers = extractNumbers(tokens)
        val consumed = mutableSetOf<Int>()

        var season: KeywordValue? = null
        var series: KeywordValue? = null

        for ((index, token) in tokens.withIndex()) {
            val isSeason = token in SEASON_KEYWORDS
            val isSeries = token in SERIES_KEYWORDS
            if (!isSeason && !isSeries) continue
            if (isSeason && season != null) continue
            if (isSeries && series != null) continue

            val number = numbers.firstOrNull { it.end == index - 1 && it.start !in consumed }
                ?: numbers.firstOrNull { it.start == index + 1 && it.start !in consumed }
                ?: continue
            consumed += number.start

            val keywordValue = KeywordValue(number.value, minOf(index, number.start))
            if (isSeason) season = keywordValue else series = keywordValue
        }

        val seasonValue = season ?: return null
        val seriesValue = series ?: return null

        val titleBoundary = minOf(seasonValue.titleBoundary, seriesValue.titleBoundary)
        if (titleBoundary <= 0) return null

        val title = tokens.subList(0, titleBoundary).joinToString(" ")
        return VoiceCommand.SetExact(title, seasonValue.value, seriesValue.value)
    }

    private fun extractNumbers(tokens: List<String>): List<NumberToken> {
        val numbers = mutableListOf<NumberToken>()
        for ((index, token) in tokens.withIndex()) {
            val value = numericValue(token) ?: continue
            val last = numbers.lastOrNull()
            if (last != null && last.end == index - 1 && canCombine(last.value, value)) {
                numbers[numbers.lastIndex] = NumberToken(last.value + value, last.start, index)
            } else {
                numbers += NumberToken(value, index, index)
            }
        }
        return numbers
    }

    private fun canCombine(left: Int, right: Int): Boolean =
        (left >= 100 && left % 100 == 0 && right < 100) ||
            (left in 20..90 && left % 10 == 0 && right in 1..9)

    private fun numericValue(token: String): Int? {
        token.toIntOrNull()?.let { return it }
        CARDINALS[token]?.let { return it }
        return ORDINAL_STEMS.firstOrNull { token.startsWith(it.first) }?.second
    }

    private data class KeywordValue(val value: Int, val titleBoundary: Int)

    private data class NumberToken(val value: Int, val start: Int, val end: Int)

    private companion object {
        val NOT_LETTER_OR_DIGIT = Regex("[^\\p{L}\\p{N}]")
        val WHITESPACE = Regex("\\s+")

        val NEW_SEASON_SUFFIXES = listOf("новый сезон", "следующий сезон")
        val NEXT_EPISODE_SUFFIXES = listOf("следующая серия", "дальше")

        val SEASON_KEYWORDS = setOf("сезон", "сезона", "сезоне", "сезонов")
        val SERIES_KEYWORDS = setOf("серия", "серии", "серию", "серией", "серий")

        val CARDINALS = mapOf(
            "один" to 1, "одна" to 1, "два" to 2, "две" to 2, "три" to 3,
            "четыре" to 4, "пять" to 5, "шесть" to 6, "семь" to 7, "восемь" to 8,
            "девять" to 9, "десять" to 10, "одиннадцать" to 11, "двенадцать" to 12,
            "тринадцать" to 13, "четырнадцать" to 14, "пятнадцать" to 15,
            "шестнадцать" to 16, "семнадцать" to 17, "восемнадцать" to 18,
            "девятнадцать" to 19, "двадцать" to 20, "тридцать" to 30, "сорок" to 40,
            "пятьдесят" to 50, "шестьдесят" to 60, "семьдесят" to 70,
            "восемьдесят" to 80, "девяносто" to 90, "сто" to 100, "двести" to 200,
            "триста" to 300, "четыреста" to 400, "пятьсот" to 500
        )

        val ORDINAL_STEMS = listOf(
            "перв" to 1, "втор" to 2, "трет" to 3, "четверт" to 4, "пят" to 5,
            "шест" to 6, "седьм" to 7, "восьм" to 8, "девят" to 9, "десят" to 10,
            "одиннадцат" to 11, "двенадцат" to 12, "тринадцат" to 13,
            "четырнадцат" to 14, "пятнадцат" to 15, "шестнадцат" to 16,
            "семнадцат" to 17, "восемнадцат" to 18, "девятнадцат" to 19,
            "двадцат" to 20, "тридцат" to 30, "сороков" to 40
        ).sortedByDescending { it.first.length }
    }
}
