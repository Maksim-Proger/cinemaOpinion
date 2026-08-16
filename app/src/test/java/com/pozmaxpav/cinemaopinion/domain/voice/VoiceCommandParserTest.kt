package com.pozmaxpav.cinemaopinion.domain.voice

import com.pozmaxpav.cinemaopinion.domain.models.voice.VoiceCommand
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VoiceCommandParserTest {

    private val parser = VoiceCommandParser()

    @Test
    fun `полная команда с числами перед ключевыми словами`() {
        assertEquals(
            VoiceCommand.SetExact("наруто", 2, 4),
            parser.parse("Наруто 2 сезон 4 серия")
        )
    }

    @Test
    fun `полная команда с числами после ключевых слов`() {
        assertEquals(
            VoiceCommand.SetExact("наруто", 2, 4),
            parser.parse("наруто сезон 2 серия 4")
        )
    }

    @Test
    fun `серия названа раньше сезона`() {
        assertEquals(
            VoiceCommand.SetExact("наруто", 2, 4),
            parser.parse("наруто 4 серия 2 сезон")
        )
    }

    @Test
    fun `название из нескольких слов`() {
        assertEquals(
            VoiceCommand.SetExact("во все тяжкие", 5, 14),
            parser.parse("Во все тяжкие 5 сезон 14 серия")
        )
    }

    @Test
    fun `большой номер серии после ключевого слова`() {
        assertEquals(
            VoiceCommand.SetExact("ван пис", 1, 1075),
            parser.parse("ван пис сезон 1 серия 1075")
        )
    }

    @Test
    fun `порядковые числительные`() {
        assertEquals(
            VoiceCommand.SetExact("наруто", 2, 4),
            parser.parse("наруто второй сезон четвертая серия")
        )
    }

    @Test
    fun `составное числительное`() {
        assertEquals(
            VoiceCommand.SetExact("доктор хаус", 3, 24),
            parser.parse("доктор хаус 3 сезон двадцать четвертая серия")
        )
    }

    @Test
    fun `падежные формы ключевых слов`() {
        assertEquals(
            VoiceCommand.SetExact("шерлок", 2, 3),
            parser.parse("шерлок 2 сезона 3 серии")
        )
    }

    @Test
    fun `пунктуация и буква е с точками`() {
        assertEquals(
            VoiceCommand.SetExact("темные начала", 2, 3),
            parser.parse("Тёмные начала, 2 сезон, 3 серия")
        )
    }

    @Test
    fun `команда дальше`() {
        assertEquals(
            VoiceCommand.NextEpisode("наруто"),
            parser.parse("Наруто дальше")
        )
    }

    @Test
    fun `команда следующая серия`() {
        assertEquals(
            VoiceCommand.NextEpisode("во все тяжкие"),
            parser.parse("во все тяжкие следующая серия")
        )
    }

    @Test
    fun `команда новый сезон`() {
        assertEquals(
            VoiceCommand.NewSeason("наруто"),
            parser.parse("Наруто новый сезон")
        )
    }

    @Test
    fun `команда следующий сезон`() {
        assertEquals(
            VoiceCommand.NewSeason("аркейн"),
            parser.parse("аркейн следующий сезон")
        )
    }

    @Test
    fun `произвольная фраза не распознается`() {
        assertNull(parser.parse("привет как дела"))
    }

    @Test
    fun `команда дальше без названия не распознается`() {
        assertNull(parser.parse("дальше"))
    }

    @Test
    fun `полная команда без названия не распознается`() {
        assertNull(parser.parse("2 сезон 4 серия"))
    }

    @Test
    fun `команда без номера серии не распознается`() {
        assertNull(parser.parse("наруто 2 сезон"))
    }

    @Test
    fun `пустая строка не распознается`() {
        assertNull(parser.parse("   "))
    }
}
