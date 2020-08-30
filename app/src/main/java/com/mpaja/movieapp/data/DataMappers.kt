package com.mpaja.movieapp.data

import com.mpaja.movieapp.data.api.model.Result
import com.mpaja.movieapp.data.repository.model.MovieModel

fun mapMoviesResponseToMovieModel(response: Result) = MovieModel(
    id = response.id,
    title = response.title,
    date = response.releaseDate,
    rating = response.voteAverage.toString(),
    overview = response.overview,
    backdropPath = response.backdropPath,
    posterPath = response.posterPath
)