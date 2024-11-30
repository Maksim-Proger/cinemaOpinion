package com.pozmaxpav.cinemaopinion.data.localdb.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "",
    val firstName: String,
    val lastName: String,
    val awards: String = "",
    val professionalPoints: Long = 0,
    val seasonalEventPoints: Long = 0
)
