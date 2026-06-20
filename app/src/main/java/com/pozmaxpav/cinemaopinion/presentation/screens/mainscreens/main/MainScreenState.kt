package com.pozmaxpav.cinemaopinion.presentation.screens.mainscreens.main

import androidx.compose.runtime.Stable
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.pozmaxpav.cinemaopinion.domain.models.api.movies.MovieData
import com.pozmaxpav.cinemaopinion.domain.models.firebase.DomainSelectedMovieModel
import com.pozmaxpav.cinemaopinion.domain.models.system.CompositeRequest

@Stable
class MainScreenState(
    val listState: LazyListState,
    val favouriteListState: LazyListState,
    val premiereListState: LazyListState,
    val topListState: LazyListState,
    var selectedDate: MutableState<Pair<Int, String>?>, // Значение выбранной даты
    var dateSelectionComplete: MutableState<Boolean>, // Флаг подтверждения, что дата выбрана и можно отправлять запрос
    var query: MutableState<String>,
    var searchBarActive: MutableState<Boolean>,
    var searchCompleted: MutableState<Boolean>, // Флаг для отображения списка фильмов после поиска
    var searchHistory: SnapshotStateList<String>,
    var requestBody: MutableState<CompositeRequest>,
    var sendRequestCompleted: MutableState<Boolean>, // Флаг для предотвращения повторной отправки запроса
    var showDatePicker: MutableState<Boolean>,
    var onAccountButtonClick: MutableState<Boolean>,
    var onAdvancedSearchButtonClick: MutableState<Boolean>,
    var locationShowDialogEvents: MutableState<Boolean>,
    var locationShowPageAppDescription: MutableState<Boolean>,
    var scrollToTop: MutableState<Boolean>,
    var menuExpanded: MutableState<Boolean>,
    var titleTopBarState: MutableState<Boolean>, // Заголовок для AppBar
    var selectedMovie: MutableState<MovieData?>,
    var selectedSeasonalMovie: MutableState<DomainSelectedMovieModel?>,
    var selectedFavoriteMovie: MutableState<DomainSelectedMovieModel?>,
)



