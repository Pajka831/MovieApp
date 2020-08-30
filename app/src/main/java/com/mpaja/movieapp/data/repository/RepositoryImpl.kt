package com.mpaja.movieapp.data.repository

import com.mpaja.movieapp.data.api.Api
import com.mpaja.movieapp.data.mapMoviesResponseToMovieModel
import com.mpaja.movieapp.data.persist.dao.FavoriteDao
import com.mpaja.movieapp.data.persist.entity.FavoriteEntity
import com.mpaja.movieapp.data.repository.model.MovieModel
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val api: Api,
    private val favoriteDao: FavoriteDao
) : Repository {

    override fun getMovies(): Single<List<MovieModel>> {
        return api.getMovies()
            .map { moviesResponse -> moviesResponse.results.map { mapMoviesResponseToMovieModel(it) } }
            .zipWith(favoriteDao.getAllFavorites(),
                BiFunction { movieList, favoriteList ->
                    return@BiFunction movieList.map { movie ->
                        favoriteList.forEach { if (movie.id == it.id) movie.favorite = true }
                        return@map movie
                    }
                })
    }

    override fun getSearchAutoCorrect(query: String): Single<List<String>> {
        return api.getSearch(query)
            .map { moviesResponse -> moviesResponse.results.take(3).map { it.title } }
    }

    override fun getSearchedMovies(query: String): Single<List<MovieModel>> {
        return api.getSearch(query)
            .map { moviesResponse -> moviesResponse.results.map { mapMoviesResponseToMovieModel(it) } }
            .zipWith(favoriteDao.getAllFavorites(),
                BiFunction { movieList, favoriteList ->
                    return@BiFunction movieList.map { movie ->
                        favoriteList.forEach { if (movie.id == it.id) movie.favorite = true }
                        return@map movie
                    }
                })
    }

    override fun handleFavorite(id: Int, isFavorite: Boolean) =
        if (isFavorite) {
            favoriteDao.deleteFavorite(FavoriteEntity(id))
        } else favoriteDao.insertFavorite(FavoriteEntity(id))
}