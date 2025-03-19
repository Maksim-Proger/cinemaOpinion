package com.pozmaxpav.cinemaopinion.data.local.room.appdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pozmaxpav.cinemaopinion.data.local.room.dao.CommentPersonalListDao
import com.pozmaxpav.cinemaopinion.data.local.room.entities.CommentPersonalListEntity

@Database(
    entities = [
        CommentPersonalListEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun commentPersonalListDao(): CommentPersonalListDao
}