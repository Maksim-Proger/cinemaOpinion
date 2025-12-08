package com.pozmaxpav.cinemaopinion.presentation.screens.screenslists

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ListWatchedMovies(
//    navController: NavHostController,
//    systemViewModel: SystemViewModel,
//    movieViewModel: MovieViewModel = hiltViewModel(),
//    userViewModel: UserViewModel = hiltViewModel()
//) {
//    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//    val listState = rememberLazyListState()
//    val context = LocalContext.current
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusManager = LocalFocusManager.current
//
//    val listMovies by movieViewModel.movies.collectAsState()
//    val stateMovies by movieViewModel.movieDownloadStatus.collectAsState()
//    val userId by systemViewModel.userId.collectAsState()
//    val userData by userViewModel.userData.collectAsState()
//
//    var showTopBar by remember { mutableStateOf(false) }
//    var selectedMovie by remember { mutableStateOf<DomainSelectedMovieModel?>(null) }
//    var selectedComment by remember { mutableStateOf<DomainCommentModel?>(null) }
//    var openBottomSheetComments by remember { mutableStateOf(false) }
//    var openBottomSheetChange by remember { mutableStateOf(false) }
//
//    val (comment, setComment) = remember { mutableStateOf("") }
//
//    LaunchedEffect(Unit) {
//        systemViewModel.getUserId()
//        movieViewModel.getMovies(NODE_LIST_WATCHED_MOVIES)
//    }
//    LaunchedEffect(userId) {
//        userViewModel.getUserData(userId)
//    }
//
//    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            if (!showTopBar) {
//                TopAppBarAllScreens(
//                    context = context,
//                    titleId = R.string.title_listWatched_movies,
//                    scrollBehavior = scrollBehavior,
//                    onTransitionAction = {
//                        navigateFunction(navController, Route.MainScreen.route)
//                    }
//                )
//            }
//        }
//    ) { innerPadding ->
//
//        if (openBottomSheetChange) {
//            CustomBottomSheet(
//                onClose = { openBottomSheetChange = false },
//                content = {
//                    userData?.let { user ->
//                        selectedMovie?.let { movie ->
//                            selectedComment?.let { comment ->
//                                ChangeComment(
//                                    dataSource = NODE_LIST_WATCHED_MOVIES,
//                                    userName = user.nikName,
//                                    selectedMovieId = movie.id,
//                                    selectedComment = comment,
//                                    viewModel = movieViewModel
//                                ) {
//                                    openBottomSheetChange = false
//                                }
//                            }
//                        }
//                    }
//                },
//                fraction = 0.5f
//            )
//            AdaptiveBackHandler { openBottomSheetChange = false }
//        }
//
//        if (openBottomSheetComments) {
//            CustomBottomSheet(
//                onClose = { openBottomSheetComments = false },
//                content = {
//                    CustomTextFieldForComments(
//                        value = comment,
//                        onValueChange = setComment,
//                        placeholder = {
//                            Text(
//                                text = stringResource(R.string.placeholder_for_comment_field),
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        },
//                        leadingIcon = {
//                            Icon(
//                                imageVector = Icons.Default.Add,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.outline
//                            )
//                        },
//                        keyboardActions = KeyboardActions(
//                            onDone = {
//                                keyboardController?.hide()
//                                focusManager.clearFocus()
//                            }
//                        )
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        CustomTextButton(
//                            textButton = stringResource(R.string.button_add),
//                            containerColor = MaterialTheme.colorScheme.secondary,
//                            contentColor = MaterialTheme.colorScheme.onSecondary,
//                            endPadding = 15.dp,
//                            onClickButton = {
//                                selectedMovie?.let { movie ->
//                                    userData?.let { user ->
//                                        movieViewModel.addComment(
//                                            dataSource = NODE_LIST_WATCHED_MOVIES,
//                                            movieId = movie.id.toDouble(),
//                                            username = user.nikName,
//                                            commentUser = comment
//                                        )
//                                        movieViewModel.createNotification(
//                                            context = context,
//                                            username = user.nikName,
//                                            stringResourceId = R.string.record_added_comment_to_movie_in_the_viewed,
//                                            title = movie.nameFilm,
//                                            newDataSource = NODE_LIST_WATCHED_MOVIES,
//                                            entityId = movie.id
//                                        )
//                                        showToast(context, R.string.comment_added)
//                                        setComment("")
//                                        openBottomSheetComments = !openBottomSheetComments
//                                    }
//                                }
//                            }
//                        )
//                    }
//                },
//                fraction = 0.7f
//            )
//            AdaptiveBackHandler { openBottomSheetComments = false }
//        }
//
//        selectedMovie?.let { movie ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.background)
//                    .padding(vertical = 45.dp)
//            ) {
//                DetailsCardSelectedMovie(
//                    movie = movie,
//                    content = {
//                        ShowCommentList(
//                            dataSource = NODE_LIST_WATCHED_MOVIES,
//                            selectedMovieId = movie.id,
//                            viewModel = movieViewModel,
//                            onClick = { comment ->
//                                selectedComment = comment
//                                openBottomSheetChange = true
//                            }
//                        )
//                    },
//                    commentButton = {
//                        CustomTextButton(
//                            textButton = context.getString(R.string.button_leave_comment),
//                            topPadding = 7.dp,
//                            bottomPadding = 7.dp,
//                            containerColor = MaterialTheme.colorScheme.secondary,
//                            contentColor = MaterialTheme.colorScheme.onSecondary,
//                            onClickButton = { openBottomSheetComments = !openBottomSheetComments }
//                        )
//                    },
//                    onClick = {
//                        selectedMovie = null
//                        showTopBar = !showTopBar
//                    }
//                )
//                AdaptiveBackHandler {
//                    selectedMovie = null
//                    showTopBar = !showTopBar
//                }
//            }
//        }
//
//        if (selectedMovie == null) {
//            when (stateMovies) {
//                is LoadingState.Loading -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CustomLottieAnimation(
//                            nameFile = "loading_animation.lottie",
//                            modifier = Modifier.scale(0.5f)
//                        )
//                    }
//                }
//
//                is LoadingState.Success -> {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(innerPadding)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(start = 10.dp)
//                                .padding(bottom = 5.dp)
//                        ) {
//                            Text(
//                                text = "${stringResource(R.string.watched_movies_sup_text)} ${listMovies.size}",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//                        LazyColumn(
//                            state = listState,
//                            modifier = Modifier.fillMaxSize(),
//                            contentPadding = PaddingValues(10.dp)
//                        ) {
//                            items(listMovies, key = { it.id }) { movie ->
//                                Row(
//                                    modifier = Modifier
//                                        .animateItem()
//                                        .fillMaxWidth(),
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Card(
//                                        modifier = Modifier
//                                            .wrapContentHeight()
//                                            .fillMaxWidth(),
//                                        colors = CardDefaults.cardColors(
//                                            containerColor = MaterialTheme.colorScheme.secondary,
//                                            contentColor = MaterialTheme.colorScheme.onSecondary
//                                        )
//                                    ) {
//                                        SelectedMovieItem(
//                                            movie = movie,
//                                            onClick = { selectedMovie = movie },
//                                            showTopBar = { showTopBar = !showTopBar }
//                                        )
//                                    }
//                                }
//                                Spacer(Modifier.padding(5.dp))
//                            }
//                        }
//                    }
//                }
//
//                is LoadingState.Error -> {
//                    // TODO: Добавить логику работы при ошибке.
//                }
//            }
//        }
//    }
//}
