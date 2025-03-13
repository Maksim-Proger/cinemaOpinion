package com.pozmaxpav.cinemaopinion.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pozmaxpav.cinemaopinion.data.local.room.entities.CommentPersonalListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentPersonalListDao {

    @Insert
    suspend fun insert(comment: CommentPersonalListEntity)

    @Query("SELECT * FROM comments")
    fun getComments(): Flow<List<CommentPersonalListEntity>>

}