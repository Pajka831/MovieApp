package com.mpaja.movieapp.data.repository

import com.mpaja.movieapp.data.repository.model.MovieModel
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun getMovies(): Single<List<MovieModel>>
    fun getSearchAutoCorrect(query: String): Single<List<String>>
    fun getSearchedMovies(query: String): Single<List<MovieModel>>
    fun handleFavorite(id: Int, isFavorite: Boolean): Completable
}