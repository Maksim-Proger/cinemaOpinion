package com.pozmaxpav.cinemaopinion.data.localdb.appdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pozmaxpav.cinemaopinion.data.localdb.converters.Converters
import com.pozmaxpav.cinemaopinion.data.localdb.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SelectedMovieDao
import com.pozmaxpav.cinemaopinion.data.localdb.dao.SeriesControlDao
import com.pozmaxpav.cinemaopinion.data.localdb.entities.CommentPersonalListEntity
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SelectedMovieEntity
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SeriesControlEntity

@Database(
    entities = [
        SelectedMovieEntity::class,
        SeriesControlEntity::class,
        CommentPersonalListEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedMovieDao(): SelectedMovieDao
    abstract fun seriesControlDao(): SeriesControlDao
    abstract fun commentPersonalListDao(): CommentPersonalListDao
}