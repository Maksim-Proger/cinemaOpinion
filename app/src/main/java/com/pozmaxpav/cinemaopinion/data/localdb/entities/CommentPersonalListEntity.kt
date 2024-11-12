package com.pozmaxpav.cinemaopinion.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentPersonalListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val movieId: Double,
    val commentText: String,
    val timestamp: Long
)
