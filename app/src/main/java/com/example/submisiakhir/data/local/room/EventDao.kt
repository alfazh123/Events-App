package com.example.submisiakhir.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.submisiakhir.data.local.entity.FavoriteEntity


@Dao
interface EventDao {

    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEntity)

    @Query("SELECT * FROM favorite WHERE id = :id")
    fun getFavoriteById(id: Int): LiveData<FavoriteEntity>

    // check if the event is bookmarked with boolean return
    @Query("SELECT * FROM favorite WHERE id = :id")
    fun isFavorite(id: Int): LiveData<FavoriteEntity>

}