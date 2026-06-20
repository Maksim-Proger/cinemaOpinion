package com.pozmaxpav.cinemaopinion.domain.models.api.movies

import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest

sealed interface SearchRequest {
    data class Keyword(val keyword: String) : SearchRequest
    data class Filters(val request: CompositeRequest) : SearchRequest
}
