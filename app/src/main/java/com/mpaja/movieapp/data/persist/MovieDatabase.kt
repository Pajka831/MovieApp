package com.mpaja.movieapp.data.persist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mpaja.movieapp.data.persist.dao.FavoriteDao
import com.mpaja.movieapp.data.persist.entity.FavoriteEntity


@Database(entities = [FavoriteEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DB_NAME = "movie_database"
    }
}