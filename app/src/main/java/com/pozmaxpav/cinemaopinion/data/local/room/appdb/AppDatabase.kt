package com.pozmaxpav.cinemaopinion.data.local.room.appdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pozmaxpav.cinemaopinion.data.local.room.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.local.room.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.local.room.entities.CommentPersonalListEntity
import com.pozmaxpav.cinemaopinion.data.local.room.entities.SelectedMovieEntity

@Database(
    entities = [
        SelectedMovieEntity::class,
        CommentPersonalListEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedMovieDao(): SelectedMovieDao
    abstract fun commentPersonalListDao(): CommentPersonalListDao
}