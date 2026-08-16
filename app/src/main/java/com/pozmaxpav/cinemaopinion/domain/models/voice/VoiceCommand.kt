package com.pozmaxpav.cinemaopinion.domain.models.voice

sealed class VoiceCommand {
    abstract val title: String

    data class SetExact(
        override val title: String,
        val season: Int,
        val series: Int
    ) : VoiceCommand()

    data class NextEpisode(
        override val title: String
    ) : VoiceCommand()

    data class NewSeason(
        override val title: String
    ) : VoiceCommand()
}
