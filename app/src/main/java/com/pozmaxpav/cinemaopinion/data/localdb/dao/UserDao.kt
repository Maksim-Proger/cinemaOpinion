package com.pozmaxpav.cinemaopinion.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pozmaxpav.cinemaopinion.data.localdb.entities.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getUser(): UserEntity

    @Update
    suspend fun updateUser(user: UserEntity)
}