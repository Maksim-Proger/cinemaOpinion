package com.pozmaxpav.cinemaopinion.utilits

sealed class Season(val node: String) {
    data object NewYear : Season(NODE_NEW_YEAR_LIST)
//    data object Halloween : Season(NODE_HALLOWEEN_LIST)
//    data object Valentine : Season(NODE_VALENTINE_LIST)
//    data object March8 : Season(NODE_MARCH_8_LIST)
//    data object Default : Season(NODE_DEFAULT_LIST)
}
