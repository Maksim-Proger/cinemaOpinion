package com.pozmaxpav.cinemaopinion.domain.repository

interface SystemSharedPreferencesRepository {
    fun saveStateSeasonalFlag(isSeasonalFlag: Boolean)
    fun getStateSeasonalFlag(): Boolean
}
