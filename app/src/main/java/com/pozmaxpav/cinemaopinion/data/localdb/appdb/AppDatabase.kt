package com.pozmaxpav.cinemaopinion.data.localdb.appdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pozmaxpav.cinemaopinion.data.localdb.dao.UserDao
import com.pozmaxpav.cinemaopinion.data.localdb.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}