package com.pozmaxpav.cinemaopinion.data.localdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.pozmaxpav.cinemaopinion.data.localdb.entities.SeriesControlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesControlDao {

    @Insert
    suspend fun insertNewMovie(movie: SeriesControlEntity)

    @Query("SELECT * FROM series_control")
    fun getListMovies(): Flow<List<SeriesControlEntity>>

    @Query("SELECT * FROM series_control WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id: Int): SeriesControlEntity?

    @Delete
    suspend fun deleteMovie(movie: SeriesControlEntity)

}