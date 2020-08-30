package com.mpaja.movieapp.data.api

import com.mpaja.movieapp.data.api.model.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("movie/now_playing")
    fun getMovies(): Single<MoviesResponse>

    @GET("search/movie")
    fun getSearch(@Query("query") query: String): Single<MoviesResponse>
}