package com.pozmaxpav.cinemaopinion.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: Надо разобраться что там у нас с id.

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val firstName: String,
    val lastName: String
)
