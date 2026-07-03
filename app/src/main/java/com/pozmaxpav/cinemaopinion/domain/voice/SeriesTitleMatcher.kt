package com.pozmaxpav.cinemaopinion.domain.voice

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel

object SeriesTitleMatcher {

    fun findBestMatch(
        entries: List<DomainSeriesControlModel>,
        spokenTitle: String
    ): DomainSeriesControlModel? {
        val target = normalize(spokenTitle)
        if (target.isEmpty()) return null

        val candidates = entries
            .map { it to normalize(it.title) }
            .filter { it.second.isNotEmpty() }

        return candidates.firstOrNull { it.second == target }?.first
            ?: candidates.filter { it.second.startsWith(target) }
                .minByOrNull { it.second.length }?.first
            ?: candidates.filter { it.second.contains(target) }
                .minByOrNull { it.second.length }?.first
    }

    private fun normalize(title: String): String =
        title.lowercase()
            .replace('ё', 'е')
            .replace(NOT_LETTER_OR_DIGIT, " ")
            .replace(WHITESPACE, " ")
            .trim()

    private val NOT_LETTER_OR_DIGIT = Regex("[^\\p{L}\\p{N}]")
    private val WHITESPACE = Regex("\\s+")
}
