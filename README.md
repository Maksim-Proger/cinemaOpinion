# Проект Cinema Opinion
## Баги, которые нужно исправить!

1) При переходе из меню настроек в "Изменить данные пользователя", меню настроек пропадает 
   с экрана уже после перехода.


# Архитектура проекта:
com.pozmaxpav.cinemaopinion
├── data
│   ├── api
│   │   ├── MovieListApi.kt
│   │   └── models
│   │       ├── ApiCountry.kt
│   │       ├── ApiGenre.kt
│   │       ├── ApiMovie.kt
│   │       ├── ApiMovieList.kt
│   │       └── ApiPagedMovieList.kt
│   ├── mappers
│   │   └── MovieMappers.kt
│   └── repository
│       └── MovieRepositoryImpl.kt
├── domain
│   ├── model
│   │   ├── Country.kt
│   │   ├── Genre.kt
│   │   ├── Movie.kt
│   │   ├── MovieList.kt
│   │   └── PagedMovieList.kt
│   ├── repository
│   │   └── MovieRepository.kt
│   └── usecase
│       ├── GetPremiereMoviesUseCase.kt
│       └── GetTopMoviesUseCase.kt
├── di
│   └── AppModule.kt
└── presentation
├── viewmodel
│   └── MainViewModel.kt
└── components

