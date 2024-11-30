package com.pozmaxpav.cinemaopinion.data.localdb.appdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pozmaxpav.cinemaopinion.data.localdb.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SeriesControlDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.UserDao
import com.pozmaxpav.cinemaopinion.data.localdb.entities.CommentPersonalListEntity
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SelectedMovieEntity
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SeriesControlEntity
import com.pozmaxpav.cinemaopinion.data.localdb.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SelectedMovieEntity::class,
        SeriesControlEntity::class,
        CommentPersonalListEntity::class
    ],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun selectedMovieDao(): SelectedMovieDao
    abstract fun seriesControlDao(): SeriesControlDao
    abstract fun commentPersonalListDao(): CommentPersonalListDao
}