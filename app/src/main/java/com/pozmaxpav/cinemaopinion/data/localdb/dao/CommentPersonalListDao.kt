package com.pozmaxpav.cinemaopinion.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pozmaxpav.cinemaopinion.data.localdb.entities.CommentPersonalListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentPersonalListDao {

    @Insert
    suspend fun insert(comment:CommentPersonalListEntity)

    @Query("SELECT * FROM comments")
    fun getComments(): Flow<List<CommentPersonalListEntity>>

}