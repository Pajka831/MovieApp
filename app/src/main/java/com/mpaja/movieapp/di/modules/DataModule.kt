package com.mpaja.movieapp.di.modules

import android.app.Application
import androidx.room.Room
import com.mpaja.movieapp.data.persist.MovieDatabase
import com.mpaja.movieapp.data.persist.dao.FavoriteDao
import com.mpaja.movieapp.data.repository.Repository
import com.mpaja.movieapp.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    abstract fun bindRepository(repository: RepositoryImpl): Repository

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideFavoriteDao(movieDatabase: MovieDatabase): FavoriteDao =
            movieDatabase.favoriteDao()

        @Provides
        @Singleton
        @JvmStatic
        fun provideDatabase(app: Application): MovieDatabase =
            Room.databaseBuilder(app, MovieDatabase::class.java, MovieDatabase.DB_NAME).build()
    }
}