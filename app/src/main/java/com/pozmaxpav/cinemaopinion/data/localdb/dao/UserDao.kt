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

    @Query("UPDATE users SET seasonalEventPoints = seasonalEventPoints + :increment WHERE id = :userId")
    suspend fun incrementSeasonalEventPoints(userId: String, increment: Long)

    @Query("SELECT awards FROM users WHERE id = :userId")
    suspend fun getAwards(userId: String): List<String>

    @Query("UPDATE users set awards = :newAwards WHERE id = :userId")
    suspend fun updateAwardsList(userId: String, newAwards: List<String>)
}