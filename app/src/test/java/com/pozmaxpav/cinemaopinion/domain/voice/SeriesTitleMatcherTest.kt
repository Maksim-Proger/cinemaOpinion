package com.pozmaxpav.cinemaopinion.domain.voice

import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSeriesControlModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SeriesTitleMatcherTest {

    private fun entry(id: String, title: String) =
        DomainSeriesControlModel(id = id, title = title)

    @Test
    fun `точное совпадение без учета регистра и пунктуации`() {
        val entries = listOf(entry("1", "Наруто: Ураганные хроники"))
        assertEquals(
            "1",
            SeriesTitleMatcher.findBestMatch(entries, "наруто ураганные хроники")?.id
        )
    }

    @Test
    fun `совпадение по началу названия`() {
        val entries = listOf(entry("1", "Наруто: Ураганные хроники"))
        assertEquals("1", SeriesTitleMatcher.findBestMatch(entries, "наруто")?.id)
    }

    @Test
    fun `совпадение по вхождению`() {
        val entries = listOf(entry("1", "Наруто: Ураганные хроники"))
        assertEquals(
            "1",
            SeriesTitleMatcher.findBestMatch(entries, "ураганные хроники")?.id
        )
    }

    @Test
    fun `точное совпадение приоритетнее частичного`() {
        val entries = listOf(
            entry("1", "Наруто: Ураганные хроники"),
            entry("2", "Наруто")
        )
        assertEquals("2", SeriesTitleMatcher.findBestMatch(entries, "наруто")?.id)
    }

    @Test
    fun `при нескольких кандидатах выбирается самое короткое название`() {
        val entries = listOf(
            entry("1", "Доктор Хаус"),
            entry("2", "Доктор Кто")
        )
        assertEquals("2", SeriesTitleMatcher.findBestMatch(entries, "доктор")?.id)
    }

    @Test
    fun `буква е с точками не мешает совпадению`() {
        val entries = listOf(entry("1", "Тёмные начала"))
        assertEquals("1", SeriesTitleMatcher.findBestMatch(entries, "темные начала")?.id)
    }

    @Test
    fun `отсутствие совпадения возвращает null`() {
        val entries = listOf(entry("1", "Наруто"))
        assertNull(SeriesTitleMatcher.findBestMatch(entries, "шерлок"))
    }

    @Test
    fun `пустой запрос возвращает null`() {
        val entries = listOf(entry("1", "Наруто"))
        assertNull(SeriesTitleMatcher.findBestMatch(entries, "  "))
    }
}
