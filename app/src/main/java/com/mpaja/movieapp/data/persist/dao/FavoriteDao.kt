package com.mpaja.movieapp.data.persist.dao

import androidx.room.*
import com.mpaja.movieapp.data.persist.entity.FavoriteEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Single<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: FavoriteEntity): Completable

    @Delete
    fun deleteFavorite(favorite: FavoriteEntity): Completable
}