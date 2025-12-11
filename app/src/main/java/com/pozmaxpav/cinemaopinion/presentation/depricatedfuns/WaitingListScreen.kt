package com.pozmaxpav.cinemaopinion.presentation.depricatedfuns

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ListWaitingContinuationSeries(
//    navController: NavHostController,
//    systemViewModel: SystemViewModel,
//    movieViewModel: MovieViewModel = hiltViewModel(),
//    userViewModel: UserViewModel = hiltViewModel(),
//    apiViewModel: ApiViewModel = hiltViewModel(),
//) {
//    val listState = rememberLazyListState()
//    val context = LocalContext.current
//
//    val listMovies by movieViewModel.movies.collectAsState()
//    val info by apiViewModel.informationMovie.collectAsState()
//    val stateMovies by movieViewModel.movieDownloadStatus.collectAsState()
//    val userId by systemViewModel.userId.collectAsState()
//    val userData by userViewModel.userData.collectAsState()
//
//    var openBottomSheetComments by remember { mutableStateOf(false) }
//    var openBottomSheetChange by remember { mutableStateOf(false) }
//    var selectedSerial by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
//    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
//    var showTopBar by remember { mutableStateOf(false) }
//
//    val isAtTop by remember {
//        derivedStateOf {
//            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        systemViewModel.getUserId()
//        movieViewModel.getMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
//        movieViewModel.observeListMovies(NODE_LIST_WAITING_CONTINUATION_SERIES)
//    }
//    LaunchedEffect(userId) {
//        userViewModel.getUserData(userId)
//    }
//    LaunchedEffect(selectedSerial) {
//        selectedSerial?.let { movie ->
//            apiViewModel.getInformationMovie(movie.id)
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//        ) {
//            if (openBottomSheetChange) {
//                CustomBottomSheet(
//                    onClose = { openBottomSheetChange = false },
//                    content = {
//                        userData?.let { user ->
//                            selectedSerial?.let { serial ->
//                                selectedComment?.let { comment ->
//                                    ChangeComment(
//                                        dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
//                                        userName = user.nikName,
//                                        selectedMovieId = serial.id,
//                                        selectedComment = comment,
//                                        viewModel = movieViewModel
//                                    ) {
//                                        openBottomSheetChange = false
//                                    }
//                                }
//                            }
//                        }
//                    },
//                    fraction = 0.5f
//                )
//                AdaptiveBackHandler { openBottomSheetChange = false }
//            }
//
//            if (openBottomSheetComments) {
//                CustomBottomSheet(
//                    onClose = {
//                        openBottomSheetComments = !openBottomSheetComments
//                    },
//                    content = {
//                        AddComment(
//                            dataUser = userData,
//                            dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
//                            newDataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
//                            movieViewModel = movieViewModel,
//                            selectedItem = selectedSerial,
//                            context = context,
//                            onClick = { openBottomSheetComments = false }
//                        )
//                    },
//                    fraction = 0.7f
//                )
//                AdaptiveBackHandler { openBottomSheetComments = false }
//            }
//
//            selectedSerial?.let { serial ->
//                userData?.let { user ->
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(MaterialTheme.colorScheme.background)
//                            .padding(vertical = 45.dp)
//                    ) {
//                        DetailsCardSelectedMovie(
//                            movie = serial,
//                            content = {
//                                ShowCommentList(
//                                    dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
//                                    selectedMovieId = serial.id,
//                                    viewModel = movieViewModel,
//                                    onClick = { comment ->
//                                        selectedComment = comment
//                                        openBottomSheetChange = true
//                                    }
//                                )
//                            },
//                            openDescription = {
//                                ExpandedCard(
//                                    title = stringResource(R.string.text_for_expandedCard_field),
//                                    description = info?.description
//                                        ?: stringResource(R.string.limit_is_over),
//                                    bottomPadding = 7.dp
//                                )
//                            },
//                            commentButton = {
//                                CustomTextButton(
//                                    textButton = context.getString(R.string.button_leave_comment),
//                                    topPadding = 7.dp,
//                                    bottomPadding = 7.dp,
//                                    containerColor = MaterialTheme.colorScheme.secondary,
//                                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                                    onClickButton = {
//                                        openBottomSheetComments = !openBottomSheetComments
//                                    }
//                                )
//                            },
//                            movieTransferButtonToWatchedMoviesList = {
//                                CustomTextButton(
//                                    textButton = context.getString(R.string.button_viewed),
//                                    topPadding = 7.dp,
//                                    containerColor = MaterialTheme.colorScheme.secondary,
//                                    contentColor = MaterialTheme.colorScheme.onSecondary,
//                                    onClickButton = {
//                                        movieViewModel.sendingToNewDirectory(
//                                            dataSource = NODE_LIST_WAITING_CONTINUATION_SERIES,
//                                            directionDataSource = NODE_LIST_WATCHED_MOVIES,
//                                            movieId = serial.id.toDouble()
//                                        )
//                                        showToast(context, R.string.series_has_been_moved_to_viewed)
//                                        movieViewModel.createNotification(
//                                            context = context,
//                                            username = user.nikName,
//                                            stringResourceId = R.string.record_series_has_been_moved_to_viewed,
//                                            title = serial.nameFilm,
//                                            newDataSource = NODE_LIST_WATCHED_MOVIES,
//                                            entityId = serial.id
//                                        )
//                                    }
//                                )
//                            },
//                            onClick = {
//                                selectedSerial = null
//                                showTopBar = !showTopBar
//                            }
//                        )
//                        AdaptiveBackHandler {
//                            selectedSerial = null
//                            showTopBar = !showTopBar
//                        }
//                    }
//                }
//            }
//
//            if (selectedSerial == null) {
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .background(MaterialTheme.colorScheme.background)
//                ) {
//                    when (stateMovies) {
//                        is LoadingState.Loading -> {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CustomLottieAnimation(
//                                    nameFile = "loading_animation.lottie",
//                                    modifier = Modifier.scale(0.5f)
//                                )
//                            }
//                        }
//
//                        is LoadingState.Success -> {
//                            LazyColumn(
//                                state = listState,
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(WindowInsets.statusBars.asPaddingValues())
//                                    .padding(top = if (isAtTop) TopAppBarDefaults.TopAppBarExpandedHeight else 0.dp),
//                                contentPadding = PaddingValues(10.dp)
//                            ) {
//                                items(listMovies, key = { it.id }) { movie ->
//
//                                    var isVisible by remember { mutableStateOf(true) }
//                                    LaunchedEffect(isVisible) {
//                                        if (!isVisible) {
//                                            movieViewModel.removeMovie(
//                                                NODE_LIST_WAITING_CONTINUATION_SERIES,
//                                                movie.id
//                                            )
//                                            movieViewModel.createNotification(
//                                                context,
//                                                userData!!.nikName,
//                                                R.string.record_deleted_the_movie,
//                                                movie.nameFilm,
//                                                "Фильм удален, страницы нет",
//                                            )
//                                        }
//                                    }
//
//                                    AnimatedVisibility(
//                                        visible = isVisible,
//                                        modifier = Modifier.animateItem(),
//                                        exit = slideOutHorizontally(
//                                            targetOffsetX = { -it },
//                                            animationSpec = tween(durationMillis = 300)
//                                        )
//                                    ) {
//                                        Card(
//                                            modifier = Modifier.wrapContentHeight(),
//                                            colors = CardDefaults.cardColors(
//                                                containerColor = MaterialTheme.colorScheme.secondary,
//                                                contentColor = MaterialTheme.colorScheme.onSecondary
//                                            )
//                                        ) {
//                                            Row(
//                                                modifier = Modifier
//                                                    .fillMaxWidth()
//                                                    .wrapContentHeight(),
//                                                horizontalArrangement = Arrangement.SpaceBetween,
//                                                verticalAlignment = Alignment.CenterVertically
//                                            ) {
//                                                Row(modifier = Modifier.weight(1f)) {
//                                                    SelectedMovieItem(
//                                                        movie = movie,
//                                                        onClick = { selectedSerial = movie },
//                                                        showTopBar = { showTopBar = !showTopBar }
//                                                    )
//                                                }
//                                                IconButton(
//                                                    onClick = { isVisible = false },
//                                                    modifier = Modifier
//                                                        .size(50.dp)
//                                                        .padding(end = 10.dp)
//                                                ) {
//                                                    Icon(
//                                                        imageVector = Icons.Default.Close,
//                                                        contentDescription = null,
//                                                        tint = MaterialTheme.colorScheme.onSecondary
//                                                    )
//                                                }
//                                            }
//                                        }
//                                    }
//                                    Spacer(Modifier.padding(5.dp))
//                                }
//                            }
//                        }
//
//                        is LoadingState.Error -> {
//                            // TODO: Добавить логику работы при ошибке.
//                        }
//                    }
//                }
//            }
//        }
//
//        if (selectedSerial == null) {
//            SpecialTopAppBar(
//                isAtTop = isAtTop,
//                title = stringResource(R.string.title_list_waiting_continuation_series),
//                goToBack = { navController.popBackStack() },
//                goToHome = { navigateFunction(navController, Route.MainScreen.route) }
//            )
//        }
//    }
//}
